## [Flamingo 2.1.0 binaries download](http://gitlab.exem-oss.org/flamingo2/flamingo2/tree/2.1.0)

## Flamingo 2

Flamingo는 Apache Hadoop EcoSystem을 기반으로한 빅데이터 인프라 환경을 보다 편리하고 분석 및 개발에 집중할 수 있으면서, 다양한 사용자들이 협업할 수 있는 환경을 제공해주는 빅데이터 플랫폼입니다.

![Flamingo 2](flamingo2-documentation/src/main/asciidoc/user-guide/korean/images/screenshot1.jpg)

![Flamingo 2](flamingo2-documentation/src/main/asciidoc/user-guide/korean/images/screenshot2.jpg)

![Flamingo 2](flamingo2-documentation/src/main/asciidoc/user-guide/korean/images/install/rstudio.png)

### 라이센스

* 듀얼 라이센스
  * 커뮤니티 라이센스 : GPL v3
  * 커머셜 라이센스 : 본 문서의 하단에 Commercial 지원 부분 참고

### 환경 요구사항

* JDK 1.7 이상
* Apache Tomcat 7 최신 버전
* Apache Hadoop 2.3 이상
* Apache Hive 0.14 이상
* MySQL 5.1 이상 (UTF-8)
* node.js

### 제공하는 기능

* 분산 파일 시스템 관리
  * 파일 시스템 브라우저
  * Audit
* R 및 RStudio 및 사용자 연동
* 모니터링
  * Resource Manager
  * Namenode 및 Datanode
  * 클러스터 노드
  * YARN Application
    * Application 강제종료
    *  Queue로 이동
  * Application 로그
  * MapReduce Job
* Pivotal HAWQ
* Apache Hive
* Apache Pig
* 워크플로우 디자이너
* 아카이브
  * YARN Application
  * MapReduce Job
* 시스템 관리
  * 메뉴 관리
  * 사용자 관리
  * 권한 관리
  * Pivotal HAWQ 사용자 관리

### ExtJS 라이센스에 대한 공지

Flamingo 2에 포함되어 있는 ExtJS 라이센스는 Cloudine Inc에서 Sencha를 통해 OEM으로 라이센스를 계약한 것으로써 임의로 추출하여 사용하는 경우 라이센스 위반이 될 수 있습니다.
Sencha에서는 오픈소스가 아닌 커머셜 지원 등의 상업적 이득을 취하는 경우 OEM 라이센스를 받도록 하고 있으므로 이에 따라서 Cloudine Inc는 그 절차에 따라서 OEM 라이센스를 취득하였습니다.
하지만 Flamingo 2 사용자는 이와 상관없이 사용하실 수 있습니다.

### 커뮤니티 라이센스

Flamingo 2를 별도 기술지원 없이 무상으로 사용하는 경우 커뮤니티 라이센스가 적용됩니다. 커뮤니티 라이센스는 오픈소스 라이센스인 GPL v3를 적용합니다.
Flamingo 2를 사용하는 사용자가 별도 기술지원없이 사용하고자 한다면 커뮤니티 라이센스를 사용하면 되며 이 경우 Cloudine Inc는 기술지원의 의무가 없습니다.
소스코드를 수정하는 경우 GPL v3에 따라서 공개의 의무가 발생하므로 이를 잘 지켜주시기 바랍니다.

### Commercial 지원
 
Flamingo 2는 이를 활용하는 고객들을 위해서 별도 기술지원을 운영하고 있습니다.
특히 Cloudine Inc는 GPL v3를 적용하지 않고 수정한 코드의 비공개, 커스터마이징, 리셀링 등의 판매, 기술지원을 받는 경우
Subscription 정책을 지원하고 있습니다(파트너사에서 필요하다면 OEM을 제공하며 이 경우 별도 계약으로 진행합니다). 
상업적 공급 및 기술지원의 경우 sales@cloudine.co.kr에 연락을 통해서 해결하시기 바랍니다.
그외의 경우 오픈소스 자체 활용은 GPL v3을 적용합니다. 

### Cloudine Inc

Cloudine Inc는 2011년부터 빅데이터 플랫폼 SW를 오픈소스로 개발하고 현장에 적용하는 빅데이터 기술기업입니다.
플랫폼 개발, Hadoop 엔지니어링, 데이터 분석(R, 데이터마이닝), 데이터 처리(MR ETL) 등의 업무를 수행하고 있으며 오픈소스를 개발하고 있습니다.
Hadoop 배포판 파트너로는 Pivotal이 있으며 Pivotal HD의 기술지원(설치, 운영, 장애 등등)을 서비스를 한국에서 제공하고 있습니다.
Flamingo, Bahamas 등의 오픈소스 프로젝트를 수행하고 있는 커미터는 Spring, ExtJS, Hadoop 부분에 전문가로써 관련 기술들을 Flamingo와 Bahamas에 적용하고 있습니다.
