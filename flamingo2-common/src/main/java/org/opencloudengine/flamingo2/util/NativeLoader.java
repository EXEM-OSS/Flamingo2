package org.opencloudengine.flamingo2.util;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * JAR 파일에 함께 패키징 되어 있는 네이티브 라이브러리를 운영체제의 임시 경로에 추출하여 네이티브 라이브러리를 로딩하는 로더.
 *
 * @author Byoung Gon, Kim
 * @version 2.0
 */
public class NativeLoader {

    private static final Log LOG = LogFactory.getLog(NativeLoader.class);

    /**
     * 네이티브 라이브러리를 추출하여 로딩한다. 추출할 네이티브 라이브러리는 시스템 모니터링에 사용하는 SIGAR API이며 이 네이티브 라이브러리는
     * OS에 따라서 다르게 추출한다. 로딩을 위해서 네이티브 라이브러리는 특정한 디렉토리에 위치해야 하므로 이를 위해서
     * JAR 파일에서 네이티브 라이브러리를 추출하여 임시 디렉토리에 저장후 이 네이티브 라이브러리를 로딩한다.
     */
    public static void loadSigarNative() {
        try {
            if (SystemUtils.IS_OS_LINUX) {
                NativeUtils.loadLibraryFromJar("/native/libsigar-amd64-linux.so");
            } else if (SystemUtils.IS_OS_MAC_OSX) {
                NativeUtils.loadLibraryFromJar("/native/libsigar-universal64-macosx.dylib");
            }
        } catch (IOException e) {
            LOG.warn("Cannot load sigar native library : " + e.getMessage());
            e.printStackTrace(); // This is probably not the best way to handle exception :-)
        }
    }
}
