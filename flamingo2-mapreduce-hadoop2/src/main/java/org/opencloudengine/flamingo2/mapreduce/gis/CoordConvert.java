/**
 * Copyright (C) 2011 Flamingo Project (http://www.cloudine.io).
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.mapreduce.gis;

import static java.lang.Math.*;

/**
 * 전국 표준 노드 링크를 위한 좌표 변환용 class.
 * TM 좌표계의 x 좌표는 경도에 해당하고
 * TM 좌표계의 y 좌표는 위도에 해당한다.
 * http://www.ngii.go.kr/kor/board/view.do?rbsIdx=31&idx=251
 * 위 링크를 참고하여 작성.
 *
 * @author Haneul, Kim
 * @since 2.1.0
 */
public class CoordConvert {

    /**
     * 장반경
     */
    private static double a = 6378137.0;

    /**
     * 편평률
     */
    private static double _f = 298.257222101;
    private static double f = 1.0 / _f;

    /**
     * 단반경
     */
    private static double b = a * (1.0 - f);

    /**
     * 원점축척계수
     */
    private static double ko = 1.0;

    /**
     * 원점가산값 dx(N)
     */
    private static double dx = 600000.0;// false northing

    /**
     * 원점가산값 dy(E)
     */
    private static double dy = 200000.0;// false easting

    /**
     * 원점위도
     */
    private static double latO = 38.0;

    /**
     * 원점위도 radian
     */
    private static double latOr = toRadians(latO);

    /**
     * 원점경도
     */
    private static double lngO = 127.0;

    /**
     * 원점경도 radian
     */
    private static double lngOr = toRadians(lngO);

    /**
     * 제1이심률
     */
    private static double e2 = (a + b) * (a - b) / pow(a, 2.0);// (a^2 - b^2)/a^2

    /**
     * 제2이심률
     */
    private static double e22 = (a + b) * (a - b) / pow(b, 2.0);// (a^2 - b^2)/b^2

    /**
     * 경도와 위도를 TM 좌표계로 변환한다.
     *
     * @param longitude 경도
     * @param latitude  위도
     * @return double[]{TM 좌표계 x, TM 좌표계 y}
     */
    public static double[] lngLat2TM(double longitude, double latitude) {
        double latr = toRadians(latitude);
        double lngr = toRadians(longitude);

        double T = pow(tan(latr), 2.0);
        double C = e2 / (1.0 - e2) * pow(cos(latr), 2.0);
        double A = (lngr - lngOr) * cos(latr);
        double N = a / (sqrt(1.0 - e2 * pow(sin(latr), 2.0)));
        double M = getM(latr);
        double Mo = getM(latOr);

        double tmX = dy + ko * N * (
                A +
                        pow(A, 3.0) / 6.0 * (1.0 - T + C) +
                        pow(A, 5.0) / 120.0 * (5.0 - 18.0 * T + pow(T, 2.0) + 72.0 * C - 58.0 * e22)
        );
        double tmY = dx + ko * (
                M - Mo + N * tan(lngr) * (
                        pow(A, 2.0) / 2.0 +
                                pow(A, 4.0) / 24.0 * (5.0 - T + 9.0 * C + 4.0 * pow(C, 2.0)) +
                                pow(A, 6.0) / 720.0 * (61.0 - 51.0 * T + pow(T, 2.0) + 600.0 * C - 330.0 * e22)
                )
        );
        return new double[]{tmX, tmY};
    }

    /**
     * TM 좌표계를 경도와 위도로 변환한다.
     *
     * @param tmY x좌표
     * @param tmX y좌표
     * @return double[]{경도(x), 위도(y)}
     */
    public static double[] tm2LngLat(double tmX, double tmY) {
        double M = getM(latOr) + (tmY - dx) / ko;
        double u1 = M / (a * (1.0 - e2 / 4.0 - 3.0 * pow(e2, 2.0) / 64.0 - 5.0 * pow(e2, 3.0) / 256.0));
        double e1 = (1 - sqrt(1 - e2)) / (1 + sqrt(1 - e2));
        double O1 = u1 +
                (3.0 / 2.0 * e1 - 27.0 / 32.0 * pow(e1, 3.0)) * sin(2.0 * u1) +
                (21.0 / 16.0 * pow(e1, 2.0) - 55.0 / 32.0 * pow(e1, 4.0)) * sin(4.0 * u1) +
                151.0 / 96.0 * pow(e1, 3.0) * sin(6.0 * u1) +
                1097.0 / 512.0 * pow(e1, 4.0) * sin(8.0 * u1);
        double R1 = a * (1.0 - e2) / (pow((1.0 - e2 * pow(sin(O1), 2.0)), 3.0 / 2.0));
        double C1 = e22 * pow(cos(O1), 2.0);
        double T1 = pow(tan(O1), 2.0);
        double N1 = a / (sqrt(1.0 - e2 * pow(sin(O1), 2.0)));
        double D = (tmX - dy) / (N1 * ko);

        double lng = lngO + toDegrees(1 / cos(O1) * (
                D -
                        pow(D, 3.0) / 6.0 * (1.0 + 2.0 * T1 + C1) +
                        pow(D, 5.0) / 120.0 * (5.0 - 2.0 * C1 + 28.0 * T1 - 3.0 * pow(C1, 2.0) + 8.0 * e22 + 24.0 * pow(T1, 2.0))
        ));
        double lat = toDegrees(O1 - N1 * tan(O1) / R1 * (
                pow(D, 2.0) / 2.0 -
                        pow(D, 4.0) / 24.0 * (5.0 + 3.0 * T1 + 10.0 * C1 - 4.0 * pow(C1, 2.0) - 9.0 * e22) +
                        pow(D, 6.0) / 720.0 * (61.0 + 90.0 * T1 + 298.0 * C1 + 45.0 * pow(T1, 2.0) - 252.0 * e22 - 3.0 * pow(C1, 2.0))
        ));
        return new double[]{lng, lat};
    }

    /**
     * 자오선장(타원둘레)을 계산한다.
     *
     * @param latr 위도 radian
     * @return 자오선장(타원둘레)
     */
    private static double getM(double latr) {
        return a * (
                (1.0 - e2 / 4.0 - 3.0 / 64.0 * pow(e2, 2.0) - 5.0 / 256.0 * pow(e2, 3.0)) * latr -
                        (3.0 / 8.0 * e2 + 3.0 / 32.0 * pow(e2, 2.0) + 45.0 / 1024.0 * pow(e2, 3.0)) * sin(2.0 * latr) +
                        (15.0 / 256.0 * pow(e2, 2.0) + 45.0 / 1024.0 * pow(e2, 3.0) * sin(4.0 * latr)) -
                        35.0 / 3072.0 * pow(e2, 3.0) * sin(6.0 * latr)
        );
    }

    /**
     * 시분초 좌표를 10진수로 변환한다.
     *
     * @param degree 각도
     * @param minute 분
     * @param second 초
     * @return 10진수
     */
    public static double degree2Decimal(double degree, double minute, double second) {
        return degree + minute / 60.0 + second / 3600.0;
    }

    /**
     * 10진수 좌표를 시분초로 변환한다.
     *
     * @param decimal 10진수
     * @return double[]{각도, 분, 초}
     */
    public static double[] decimal2Degree(double decimal) {
        double degree = (int) decimal;
        double minute = (int) ((decimal - degree) * 60.0);
        double second = ((decimal - degree) * 60.0 - minute) * 60.0;
        return new double[]{degree, minute, second};
    }
}