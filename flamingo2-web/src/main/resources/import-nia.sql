CREATE DATABASE flamingo2 CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI;
CREATE USER 'flamingo'@'localhost' IDENTIFIED BY 'flamingo';
GRANT ALL PRIVILEGES ON *.* TO 'flamingo'@'localhost';
FLUSH PRIVILEGES;

USE flamingo2;

CREATE TABLE IF NOT EXISTS flamingo2.FL_AUTHORITIES (
  ID              SMALLINT NOT NULL AUTO_INCREMENT,
  AUTHORITY       VARCHAR(100) NOT NULL,
  AUTHORITY_NM    VARCHAR(100) NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE KEY (AUTHORITY)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

INSERT INTO flamingo2.FL_AUTHORITIES (AUTHORITY, AUTHORITY_NM) VALUES ('ROLE_ADMIN', '관리자');
INSERT INTO flamingo2.FL_AUTHORITIES (AUTHORITY, AUTHORITY_NM) VALUES ('ROLE_USER', '사용자');

CREATE TABLE IF NOT EXISTS flamingo2.FL_ORG (
  ID              BIGINT NOT NULL AUTO_INCREMENT,
  ORG_CD          VARCHAR(255),
  ORG_NM          VARCHAR(255),
  DESCRIPTION     LONGTEXT,
  REG_DT          TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Registered Date',
  UPD_DT          TIMESTAMP     NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'Updated Date',
  PRIMARY KEY (ID)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

INSERT INTO flamingo2.FL_ORG (ID, ORG_CD, ORG_NM, DESCRIPTION, UPD_DT)
VALUES (1, 'OCE', 'Cloudine', 'Cloudine', CURRENT_TIMESTAMP);

INSERT INTO flamingo2.FL_ORG (ID, ORG_CD, ORG_NM, DESCRIPTION, UPD_DT)
VALUES (2, 'OCE', 'Demo', '데모', CURRENT_TIMESTAMP);

INSERT INTO flamingo2.FL_ORG (ID, ORG_CD, ORG_NM, DESCRIPTION, UPD_DT)
VALUES (3, 'HDFS', 'Hdfs', 'hdfs', CURRENT_TIMESTAMP);

CREATE TABLE IF NOT EXISTS flamingo2.FL_USER_LEVEL (
  LEVEL             SMALLINT NOT NULL,
  LEVEL_NM          VARCHAR(30) NOT NULL,
  PRIMARY KEY (LEVEL)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

INSERT INTO flamingo2.FL_USER_LEVEL (LEVEL, LEVEL_NM) VALUES (1, '1등급');
INSERT INTO flamingo2.FL_USER_LEVEL (LEVEL, LEVEL_NM) VALUES (2, '2등급');
INSERT INTO flamingo2.FL_USER_LEVEL (LEVEL, LEVEL_NM) VALUES (3, '3등급');
INSERT INTO flamingo2.FL_USER_LEVEL (LEVEL, LEVEL_NM) VALUES (4, '4등급');
INSERT INTO flamingo2.FL_USER_LEVEL (LEVEL, LEVEL_NM) VALUES (5, '5등급');

CREATE TABLE IF NOT EXISTS flamingo2.FL_USER (
  ID                BIGINT          NOT NULL AUTO_INCREMENT,
  USER_NM           VARCHAR(255)    NOT NULL,
  PASSWD            VARCHAR(255)    NOT NULL,
  EMAIL             VARCHAR(255)    NOT NULL,
  NM                VARCHAR(255)    NOT NULL,
  ORG_ID            BIGINT          NOT NULL DEFAULT 1 COMMENT '소속 ID',
  AUTH_ID           SMALLINT        NOT NULL DEFAULT 2 COMMENT '권한 ID',
  LEVEL             SMALLINT        NOT NULL DEFAULT 5 COMMENT '회원 등급',
  ENABLED           BOOLEAN         NOT NULL DEFAULT FALSE  COMMENT '관리자에 의해 승인 처리된 회원 상태',
  REG_DT            TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Registered Date',
  UPD_DT            TIMESTAMP       NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'Updated Date',
  LINUX_USER_HOME   VARCHAR(255)    COMMENT '리눅스 사용자 홈 디렉토리 경로',
  HDFS_USER_HOME    VARCHAR(255)    COMMENT 'HDFS 사용자 홈 디렉토리 경로',
  USER_GROUP        VARCHAR(255)    COMMENT '리눅스 및 HDFS에 사용되는 사용자 그룹',
  DESCRIPTION       LONGTEXT        COMMENT '사용자 계정 설명',
  FOREIGN KEY (ORG_ID) REFERENCES flamingo2.FL_ORG(ID),
  FOREIGN KEY (AUTH_ID) REFERENCES flamingo2.FL_AUTHORITIES(ID),
  FOREIGN KEY (LEVEL) REFERENCES flamingo2.FL_USER_LEVEL(LEVEL),
  PRIMARY KEY (ID),
  UNIQUE KEY (USER_NM)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

INSERT INTO flamingo2.FL_USER (ID, USER_NM, PASSWD, EMAIL, NM, DESCRIPTION, LINUX_USER_HOME, HDFS_USER_HOME, USER_GROUP, UPD_DT, ENABLED, ORG_ID, AUTH_ID, LEVEL)
VALUES (1, 'admin', 'MEVd1+d7s2DoZt8mgx+1kg==', 'admin@cloudine.co.kr', 'System Admin', 'System Admin', '/data1/admin', '/user/admin', 'admin', CURRENT_TIMESTAMP, 1, 1, 1, 1);

INSERT INTO flamingo2.FL_USER (ID, USER_NM, PASSWD, EMAIL, NM, DESCRIPTION, LINUX_USER_HOME, HDFS_USER_HOME, USER_GROUP, UPD_DT, ENABLED, ORG_ID, AUTH_ID, LEVEL)
VALUES (2, 'cloudine', '4/Yw449MyCSmvmOJUfn5Iw==', 'all@cloudine.co.kr', 'Cloudine', 'Cloudine', '/data1/cloudine', '/user/cloudine', 'cloudine',  CURRENT_TIMESTAMP, 1, 1, 2, 2);

INSERT INTO flamingo2.FL_USER (ID, USER_NM, PASSWD, EMAIL, NM, DESCRIPTION, LINUX_USER_HOME, HDFS_USER_HOME, USER_GROUP, UPD_DT, ENABLED, ORG_ID, AUTH_ID, LEVEL)
VALUES (3, 'demo', 'XeOvVrkafW4YJe3tni7ngQ==', 'all@cloudine.co.kr', 'Demo', 'Demo', '/data1/demo', '/user/demo', 'demo', CURRENT_TIMESTAMP, 1, 1, 2, 2);

INSERT INTO flamingo2.FL_USER (ID, USER_NM, PASSWD, EMAIL, NM, DESCRIPTION, LINUX_USER_HOME, HDFS_USER_HOME, USER_GROUP, UPD_DT, ENABLED, ORG_ID, AUTH_ID, LEVEL)
VALUES (4, 'hdfs', 'XeOvVrkafW4YJe3tni7ngQ==', 'all@cloudine.co.kr', 'Demo', 'Demo', '/data1/demo', '/user/demo', 'demo', CURRENT_TIMESTAMP, 1, 1, 2, 2);


CREATE TABLE IF NOT EXISTS flamingo2.FL_USER_AUTH (
  USER_ID              BIGINT NOT NULL,
  AUTH_ID              SMALLINT NOT NULL DEFAULT 2,
  PRIMARY KEY (USER_ID, AUTH_ID),
  FOREIGN KEY (USER_ID) REFERENCES flamingo2.FL_USER(ID) ON DELETE CASCADE,
  FOREIGN KEY (AUTH_ID) REFERENCES flamingo2.FL_AUTHORITIES(ID)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

INSERT INTO flamingo2.FL_USER_AUTH (USER_ID, AUTH_ID) VALUES(1, 1);
INSERT INTO flamingo2.FL_USER_AUTH (USER_ID, AUTH_ID) VALUES(1, 2);
INSERT INTO flamingo2.FL_USER_AUTH (USER_ID, AUTH_ID) VALUES(2, 2);
INSERT INTO flamingo2.FL_USER_AUTH (USER_ID, AUTH_ID) VALUES(3, 2);

CREATE TABLE IF NOT EXISTS flamingo2.FL_HDFS_PATH_AUTH (
  ID                    BIGINT        NOT NULL AUTO_INCREMENT,
  HDFS_PATH_PATTERN     VARCHAR(255)  NOT NULL COMMENT 'HDFS Path Pattern',
  CREATE_DIR   			BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Create Directory',
  COPY_DIR     			BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Copy Directory',
  MOVE_DIR     			BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Move Directory',
  RENAME_DIR  			BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Rename Directory',
  DELETE_DIR    		BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Delete Directory',
  MERGE_DIR    		    BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Merge All Files In Directory',
  PERMISSION_DIR    	BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Set Permission To Directories And Files',
  CREATE_DB_DIR     	BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Create Hive Database',
  CREATE_TABLE_DIR  	BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Create Hive Table',
  COPY_FILE     		BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Copy File',
  MOVE_FILE     		BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Move File',
  RENAME_FILE  			BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Rename File',
  DELETE_FILE    		BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Delete File',
  UPLOAD_FILE    		BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Upload File',
  DOWNLOAD_FILE     	BOOLEAN       NOT NULL DEFAULT FALSE COMMENT 'Download File',
  VIEW_FILE  	        BOOLEAN		  NOT NULL DEFAULT FALSE COMMENT 'View Contents Into A File',
  PERMISSION_FILE  	    BOOLEAN		  NOT NULL DEFAULT FALSE COMMENT 'Set Permission To Files',
  COPY_TO_LOCAL_FILE  	BOOLEAN		  NOT NULL DEFAULT FALSE COMMENT 'Copy Directories And Files From HDFS To Local',
  REG_DT                TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Registered Date',
  UPD_DT                TIMESTAMP     NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'Updated Date',
  AUTH_ID				SMALLINT      NOT NULL DEFAULT 2 COMMENT 'Authority ID',
  LEVEL                 SMALLINT	  NOT NULL DEFAULT 5 COMMENT 'User Level',
  PRIMARY KEY (ID),
  FOREIGN KEY (AUTH_ID) REFERENCES FL_AUTHORITIES (ID),
  FOREIGN KEY (LEVEL) REFERENCES FL_USER_LEVEL (LEVEL),
  UNIQUE KEY (HDFS_PATH_PATTERN, AUTH_ID, LEVEL)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

INSERT INTO flamingo2.FL_HDFS_PATH_AUTH (
  ID,
  HDFS_PATH_PATTERN,
  CREATE_DIR, COPY_DIR, MOVE_DIR, RENAME_DIR, DELETE_DIR, MERGE_DIR, PERMISSION_DIR, CREATE_DB_DIR, CREATE_TABLE_DIR,
  COPY_FILE, MOVE_FILE, RENAME_FILE, DELETE_FILE, UPLOAD_FILE, DOWNLOAD_FILE, VIEW_FILE, PERMISSION_FILE, COPY_TO_LOCAL_FILE,
  UPD_DT, AUTH_ID, LEVEL)
VALUES (1, '/**', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1);

INSERT INTO flamingo2.FL_HDFS_PATH_AUTH (
  ID,
  HDFS_PATH_PATTERN,
  CREATE_DIR, COPY_DIR, MOVE_DIR, RENAME_DIR, DELETE_DIR, MERGE_DIR, PERMISSION_DIR, CREATE_DB_DIR, CREATE_TABLE_DIR,
  COPY_FILE, MOVE_FILE, RENAME_FILE, DELETE_FILE, UPLOAD_FILE, DOWNLOAD_FILE, VIEW_FILE, PERMISSION_FILE, COPY_TO_LOCAL_FILE,
  UPD_DT, AUTH_ID, LEVEL)
VALUES (2, '/user/cloudine/**', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 2, 2);

INSERT INTO flamingo2.FL_HDFS_PATH_AUTH (
  ID,
  HDFS_PATH_PATTERN,
  CREATE_DIR, COPY_DIR, MOVE_DIR, RENAME_DIR, DELETE_DIR, MERGE_DIR, PERMISSION_DIR, CREATE_DB_DIR, CREATE_TABLE_DIR,
  COPY_FILE, MOVE_FILE, RENAME_FILE, DELETE_FILE, UPLOAD_FILE, DOWNLOAD_FILE, VIEW_FILE, PERMISSION_FILE, COPY_TO_LOCAL_FILE,
  UPD_DT, AUTH_ID, LEVEL)
VALUES (3, '/user/demo/**', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 2, 2);

CREATE TABLE IF NOT EXISTS flamingo2.FL_ADMIN_MENU_AUTH (
  USERLEVEL       VARCHAR(20),
  MENU_ID         VARCHAR(20),
  REG_DT          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  REG_USER_ID     BIGINT,
  UPD_DT          TIMESTAMP DEFAULT '0000-00-00 00:00:00',
  UPD_USER_ID     BIGINT
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS flamingo2.FL_ADMIN_MENU_MNG (
  MENU_ID             VARCHAR(20),
  MENU_NM             VARCHAR(1000),
  MENU_NS             VARCHAR(200),
  PARENTS_MENU_ID     VARCHAR(20),
  SORT_ORDR           INT(11),
  USE_AT              VARCHAR(1),
  REG_DT              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  REG_USER_ID         BIGINT,
  UPD_DT              TIMESTAMP DEFAULT '0000-00-00 00:00:00',
  UPD_USER_ID         BIGINT
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS flamingo2.FL_ADMIN_USER_LEVEL (
  ID                BIGINT NOT NULL AUTO_INCREMENT,
  LEVEL_ID          VARCHAR(30) NOT NULL,
  LEVEL_NM          VARCHAR(30) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS flamingo2.FL_SYSTEM_EVENTS (
  ID                 BIGINT NOT NULL AUTO_INCREMENT,
  CATEGORY           VARCHAR(255),
  SERVICE            VARCHAR(255),
  ACTION             VARCHAR(255),
  REG_DT             TIMESTAMP,
  STATUS             VARCHAR(255),
  MESSAGE            LONGTEXT,
  CAUSE              LONGTEXT,
  EXCEPTION          LONGTEXT,
  IDENTIFIER         VARCHAR(255),
  USERNAME           VARCHAR(255),
  YYYY               VARCHAR(8),
  MM                 VARCHAR(8),
  DD                 VARCHAR(8),
  PRIMARY KEY (ID),
  UNIQUE KEY (IDENTIFIER)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS flamingo2.FL_TREE (
  ID            INT(11)      NOT NULL AUTO_INCREMENT COMMENT 'Sequence',
  NAME          VARCHAR(250) NOT NULL COMMENT 'Name',
  TREE          VARCHAR(10)  NOT NULL COMMENT 'Tree Type',
  NODE          VARCHAR(10)  NOT NULL COMMENT 'Node Type',
  ROOT          BOOLEAN      DEFAULT NULL COMMENT 'Username',
  USERNAME      VARCHAR(50)  NOT NULL COMMENT 'Username',
  PARENT_ID     INT(11)      DEFAULT NULL COMMENT 'Parent',
  FOREIGN KEY(PARENT_ID) REFERENCES flamingo2.FL_TREE(ID),
  PRIMARY KEY (ID)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

INSERT INTO flamingo2.FL_TREE (ID, NAME, TREE, NODE, ROOT, USERNAME)
VALUES (1, '/', 'WORKFLOW', 'FOLDER', true, 'admin');
INSERT INTO flamingo2.FL_TREE (ID, NAME, TREE, NODE, ROOT, USERNAME)
VALUES (2, '/', 'WORKFLOW', 'FOLDER', true, 'cloudine');
INSERT INTO flamingo2.FL_TREE (ID, NAME, TREE, NODE, ROOT, USERNAME)
VALUES (3, '/', 'WORKFLOW', 'FOLDER', true, 'demo');

CREATE TABLE IF NOT EXISTS flamingo2.FL_WORKFLOW (
  ID            INT(11)      NOT NULL AUTO_INCREMENT COMMENT 'Sequence',
  WORKFLOW_ID   VARCHAR(60)  NOT NULL COMMENT 'Workflow String ID',
  WORKFLOW_NAME VARCHAR(250) NOT NULL COMMENT 'Workflow Name',
  DESCRIPTION   VARCHAR(250) DEFAULT '' COMMENT 'Description',
  VARIABLE      LONGTEXT     DEFAULT NULL COMMENT 'Workflow Variable',
  WORKFLOW_XML  LONGTEXT     NOT NULL COMMENT 'Workflow XML',
  DESIGNER_XML  LONGTEXT     NOT NULL COMMENT 'Designer XML',
  CREATE_DT     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT 'Workflow Variable',
  STATUS        VARCHAR(10)  NOT NULL COMMENT 'Workflow Variable',
  TREE_ID       INT(11)      NOT NULL COMMENT 'Tree ID',
  USERNAME      VARCHAR(50)  NOT NULL COMMENT 'Writer',
  PRIMARY KEY (ID)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

--
-- Menu
--

CREATE TABLE IF NOT EXISTS flamingo2.FL_COMM_MENU
(
  MENU_ID	 VARCHAR(20)    NOT NULL COMMENT 'Menu ID',
  MENU_NM	 VARCHAR(100)    NOT NULL COMMENT 'Menu Name',
  MENU_NS	 VARCHAR(150)    NOT NULL COMMENT 'Menu Namespace',
  PRNTS_MENU_ID	 VARCHAR(20)    NOT NULL COMMENT 'Parent Menu ID',
  SORT_ORDR	 INTEGER   NOT NULL COMMENT 'Order',
  USE_YN	 CHAR(1)    NOT NULL COMMENT 'Use',
  ICON_CSS_NM	 VARCHAR(100)    NOT NULL COMMENT 'Icon CSS',
  MENU_NM_ko_KR	 VARCHAR(100)    NULL COMMENT 'ko_KR',
  MENU_NM_en_US	 VARCHAR(100)    NULL COMMENT 'en_US',
  MENU_NM_ja_JP	 VARCHAR(100)    NULL COMMENT 'ja_JP',
  MENU_NM_zh_CN	 VARCHAR(100)    NULL COMMENT 'zh_CN',
  PRIMARY KEY ( MENU_ID )
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

INSERT INTO flamingo2.FL_COMM_MENU VALUES
('MN001','모니터링','monitoring','TOP',1,'Y','menu-monitoring','모니터링','Monitoring','Monitoring','Monitoring'),
('MN001001','리소스 관리자','monitoring.resourcemanager.ResourceManager','MN001',1,'Y','','리소스 관리자','Resource Manager','Resource Manager','Resource Manager'),
('MN001002','YARN 애플리케이션','monitoring.applications.YarnApplication','MN001',2,'Y','','YARN 애플리케이션','Yarn Application','Yarn Application','Yarn Application'),
('MN001003','MapReduce','monitoring.historyserver.HistoryServer','MN001',3,'Y','','MapReduce','MapReduce','MapReduce','MapReduce'),
('MN001004','클러스터 노드','monitoring.clusternode.ClusterNode','MN001',5,'Y','','클러스터 노드','Clusternode','Clusternode','Clusternode'),
('MN001005','네임노드','monitoring.namenode.Namenode','MN001',6,'Y','','네임노드','Namenode','Namenode','Namenode'),
('MN001006','데이터노드','monitoring.datanode.Datanode','MN001',7,'Y','','데이터노드','Datanode','Datanode','Datanode'),
('MN001007','Apache Spark','monitoring.spark.Spark','MN001',8,'Y','','Apache Spark','Apache Spark','Apache Spark','Apache Spark'),
('MN002','실시간 스트림 분석','realtime','TOP',2,'Y','menu-visual','실시간 스트림 분석','Realtime Stream Analysis','Realtime Stream Analysis','Realtime Stream Analysis'),
('MN002001','D2','realtime.d2.D2','MN002',1,'Y','','D2','D2','D2','D2'),
('MN002002','Spark Streaming','realtime.spark.streaming.SparkStreaming','MN002',2,'Y','','Spark Streaming','Spark Streaming','Spark Streaming','Spark Streaming'),
('MN002003','Esper','realtime.esper.Esper','MN002',3,'Y','','Esper','Esper','Esper','Esper'),
('MN003','워크플로우','designer','TOP',3,'Y','menu-workflow','워크플로우','Workflow','Workflow','Workflow'),
('MN003001','워크플로우 디자이너','designer.Designer','MN003',3,'Y','fa-picture-o','워크플로우 디자이너','Workflow Designer','Workflow Designer','Workflow Designer'),
('MN003002','워크플로우 모니터링','dashboard.Dashboard','MN003',3,'Y','fa-pie-chart','워크플로우 모니터링','Workflow Monitoring','Workflow Monitoring','Workflow Monitoring'),
('MN004','배치 작업 관리','batch.Job','TOP',4,'Y','menu-batch','배치 작업 관리','Batch Job Management','Batch Job Management','Batch Job Management'),
('MN005','파일 시스템 관리','fs','TOP',5,'Y','menu-filesystem','파일 시스템 관리','File System Management','File System Management','File System Management'),
('MN005001','HDFS 브라우저','fs.hdfs.Browser','MN005',1,'Y','','HDFS 브라우저','HDFS Browser','HDFS Browser','HDFS Browser'),
('MN005002','HDFS Audit 로그','fs.audit.Audit','MN005',2,'Y','','HDFS Audit 로그','HDFS Audit Log','HDFS Audit Log','HDFS Audit Log'),
('MN006','Apache Hive','hive.Hive','TOP',6,'Y','menu-hive','Apache Hive','Apache Hive','Apache Hive','Apache Hive'),
('MN007','Apache Pig','pig.Pig','TOP',7,'Y','menu-pig','Apache Pig','Apache Pig','Apache Pig','Apache Pig'),
('MN009','RStudio','r.RStudio','TOP',9,'Y','menu-rstudio','RStudio','RStudio','RStudio','RStudio'),
('MN010','시각화','visualization.ggplot2.Ggplot2','TOP',10,'Y','menu-visual','시각화','Visualization','Visualization','Visualization'),
('MN011','SQL on Hadoop','','TOP',11,'Y','menu-hawq','SQL on Hadoop','SQL on Hadoop','SQL on Hadoop','SQL on Hadoop'),
('MN011001','Pivotal HAWQ','hawq.Hawq','MN011',1,'Y','','Pivotal HAWQ','Pivotal HAWQ','Pivotal HAWQ','Pivotal HAWQ'),
('MN011002','Apache Tajo','tajo.Tajo','MN011',2,'Y','','Apache Tajo','Apache Tajo','Apache Tajo','Apache Tajo'),
('MN012','아카이브','archive','TOP',12,'Y','menu-archive','아카이브','Archive','Archive','Archive'),
('MN012001','YARN 애플리케이션','archive.yarn.ArchiveYarnApplication','MN012',1,'Y','','YARN 애플리케이션','YARN Application','YARN Application','YARN Application'),
('MN012002','MapReduce','archive.mapreduce.ArchiveMapReduce','MN012',2,'Y','','MapReduce','MapReduce','MapReduce','MapReduce'),
('MN013','터미널','terminal.Terminals','TOP',13,'Y','menu-terminal','터미널','Terminal','Terminal','Terminal'),
('MN014','UIMA','uima.Uima','TOP',14,'Y','menu-visual','UIMA','UIMA','UIMA','UIMA'),
('MN099','시스템 관리','system','TOP',99,'Y','menu-system','시스템 관리','System Management','System Management','System Management'),
('MN099001','다국어 관리','system.language.Language','MN099',1,'Y','','다국어 관리','Language Management','Language Management','Language Management'),
('MN099002','메뉴 관리','system.menu.Menu','MN099',1,'Y','','메뉴 관리','Menu Management','Menu Management','Menu Management'),
('MN099003','사용자 관리','system.user.User','MN099',1,'Y','','사용자 관리','User Management','User Management','User Management'),
('MN099004','HDFS 브라우저 권한 관리','system.authority.HdfsBrowserAuthority','MN099',1,'Y','','HDFS 브라우저 권한 관리','HDFS Browser Authority','HDFS Browser Authority','HDFS Browser Authority'),
('MN099005','HAWQ 권한 관리','system.hawq.HawqAuth','MN099',1,'Y','','HAWQ 권한 관리','HAWQ Authority','HAWQ Authority','HAWQ Authority'),
('MN099006','라이센스','system.license.License','MN099',1,'Y','','라이센스','License','License','License');


-- 시연용 Insert Values

INSERT INTO flamingo2.FL_COMM_MENU VALUES
  ('MN001','Apache Spark','monitoring.spark.Spark','TOP',1,'Y','menu-monitoring','Apache Spark','Apache Spark','Apache Spark','Apache Spark'),
  ('MN002','UIMA','uima.Uima','TOP',2,'Y','menu-visual','UIMA','UIMA','UIMA','UIMA'),
  ('MN003','리소스 관리자','monitoring.resourcemanager.ResourceManager','TOP',3,'Y','menu-monitoring','리소스매니저','Resource Manager','Resource Manager','Resource Manager'),
  ('MN004','YARN 애플리케이션','monitoring.applications.YarnApplication','TOP',4,'Y','menu-monitoring','Yarn 애플리케이션','Yarn Application','Yarn Application','Yarn Application'),
  ('MN005','MapReduce','monitoring.historyserver.HistoryServer','TOP',5,'Y','menu-monitoring','맵리듀스','MapReduce','MapReduce','MapReduce'),
  ('MN006','클러스터 노드','monitoring.clusternode.ClusterNode','TOP',6,'Y','menu-monitoring','클러스터노드','Clusternode','Clusternode','Clusternode'),
  ('MN007','네임노드','monitoring.namenode.Namenode','TOP',7,'Y','menu-monitoring','네임노드','Namenode','Namenode','Namenode'),
  ('MN008','데이터노드','monitoring.datanode.Datanode','TOP',8,'Y','menu-monitoring','데이터노드','Datanode','Datanode', 'Datanode'),
  ('MN009','파일 시스템 관리','fs','TOP',9,'Y','menu-filesystem','파일 시스템 관리','File System Management','File System Management','File System Management'),
  ('MN005001','HDFS 브라우저','fs.hdfs.Browser','MN009',1,'Y','','HDFS 브라우저','HDFS Browser','HDFS Browser','HDFS Browser'),
  ('MN005002','HDFS Audit 로그','fs.audit.Audit','MN009',2,'Y','','HDFS Audit 로그','HDFS Audit Log','HDFS Audit Log','HDFS Audit Log'),
  ('MN010','Apache Hive','hive.Hive','TOP',10,'Y','menu-hive','Apache Hive','Apache Hive','Apache Hive','Apache Hive'),
  ('MN011','아카이브','archive','TOP',11,'Y','menu-archive','아카이브','Archive','Archive','Archive'),
  ('MN012001','YARN 애플리케이션','archive.yarn.ArchiveYarnApplication','MN011',1,'Y','','YARN 애플리케이션','YARN Application','YARN Application','YARN Application'),
  ('MN012002','MapReduce','archive.mapreduce.ArchiveMapReduce','MN011',2,'Y','','MapReduce','MapReduce','MapReduce','MapReduce');




CREATE TABLE flamingo2.FL_FS_AUDIT (
  ID              INT(11)      NOT NULL AUTO_INCREMENT COMMENT 'Sequence',
  CLUSTER_ID      VARCHAR(250) DEFAULT NULL COMMENT 'Hadoop Cluster Identifier',
  CLUSTER_NAME    VARCHAR(250) DEFAULT NULL COMMENT 'Hadoop Cluster Name',
  FROM_PATH       TEXT         DEFAULT NULL COMMENT 'From Path',
  TO_PATH         TEXT         DEFAULT NULL COMMENT 'To Path',
  LENGTH          LONG         DEFAULT NULL COMMENT 'Total File Size',
  FS_TYPE         VARCHAR(50)  NOT NULL COMMENT 'File System Type (HDFS, LOCAL)',
  AUDIT_TYPE      VARCHAR(50)  NOT NULL COMMENT 'Audit Type',
  FILE_TYPE       VARCHAR(50)  NOT NULL COMMENT 'File Type',
  WORK_DATE       DATETIME     COMMENT 'Work Date',
  REQ_TYPE        VARCHAR(50)  NOT NULL COMMENT 'Request Type (CLI, UI)',
  YYYY            VARCHAR(12)  NOT NULL COMMENT 'Year',
  MM              VARCHAR(12)  NOT NULL COMMENT 'Month',
  DD              VARCHAR(12)  NOT NULL COMMENT 'Day',
  USERNAME        VARCHAR(50)  NOT NULL COMMENT 'Writer',
  PRIMARY KEY (ID)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

--
-- YARN Application Monitoring
--

CREATE TABLE IF NOT EXISTS flamingo2.FL_CL_YARN (
    ID               INT(11) NOT NULL AUTO_INCREMENT,
    SYSTEM           VARCHAR(255) DEFAULT NULL,
    APP_ID           VARCHAR(255) DEFAULT NULL,
    APP_TYPE         VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (ID),
    UNIQUE KEY (SYSTEM, APP_ID, APP_TYPE)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- YARN Application Monitoring
--

CREATE TABLE IF NOT EXISTS flamingo2.FL_CL_YARN_DUMP (
    ID                          INT(11) NOT NULL AUTO_INCREMENT,
    SYSTEM                      VARCHAR(255) DEFAULT NULL,
    APPLICATION_ID              VARCHAR(255) DEFAULT NULL,
    APPLICATION_TYPE            VARCHAR(255) DEFAULT NULL,
    PROGRESS                    VARCHAR(10) DEFAULT NULL,
    QUEUE                       VARCHAR(255) DEFAULT NULL,
    MEMORY_SECONDS              INT(11) NULL,
    RPC_PORT                    INT(11) NOT NULL,
    AM_HOST                     VARCHAR(255) DEFAULT NULL,
    USED_RESOURCES_MEMORY       INT(11) NULL,
    START_TIME                  DATETIME,
    RESERVED_RESOURCES_VCORES   INT(11) NULL,
    RESERVED_RESOURCES_MEMORY   INT(11) NULL,
    TRACKING_URL                LONGTEXT DEFAULT NULL,
    YARN_APPLICATION_STATE      VARCHAR(255) DEFAULT NULL,
    NEEDED_RESOURCES_VCORES     INT(11) NULL,
    NAME                        LONGTEXT DEFAULT NULL,
    NUM_RESERVED_CONTAINERS     INT(11) NULL,
    USED_RESOURCES_VCORES       INT(11) NULL,
    FINISH_TIME                 DATETIME,
    NUM_USED_CONTAINERS         INT(11) NOT NULL,
    FINAL_APPLICATION_STATUS    VARCHAR(255) DEFAULT NULL,
    USER                        VARCHAR(255) DEFAULT NULL,
    NEEDED_RESOURCES_MEMORY     INT(11) NULL,
    VCORE_SECONDS               INT(11) NULL,
    DIAGNOSTICS                 LONGTEXT DEFAULT NULL,
    LOG                         LONGTEXT DEFAULT NULL,
    REGISTERED_DATE             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX IDX_FL_CL_YARN_DUMP_01 ON flamingo2.FL_CL_YARN_DUMP (APPLICATION_ID);

--
-- MapReduce Job Monitoring
--

CREATE TABLE IF NOT EXISTS flamingo2.FL_CL_MR (
    ID               INT(11) NOT NULL AUTO_INCREMENT,
    SYSTEM           VARCHAR(255) DEFAULT NULL,
    JOB_ID           VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (ID),
    UNIQUE KEY (SYSTEM, JOB_ID)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- MapReduce Job Monitoring
--

CREATE TABLE IF NOT EXISTS flamingo2.FL_CL_MR_DUMP (
    ID                     INT(11) NOT NULL AUTO_INCREMENT,
    SYSTEM                 VARCHAR(255) DEFAULT NULL,
    JOB_ID                 VARCHAR(255) DEFAULT NULL,
    NAME                   LONGTEXT DEFAULT NULL,
    QUEUE                  VARCHAR(255) DEFAULT NULL,
    USER                   VARCHAR(255) DEFAULT NULL,
    STATE                  VARCHAR(255) DEFAULT NULL,
    USERNAME               VARCHAR(255) DEFAULT '',
    TYPE                   VARCHAR(255) DEFAULT 'MAPREDUCE',
    MAPS_TOTAL             INT(11) DEFAULT 0,
    MAPS_COMPLETED         INT(11) DEFAULT 0,
    REDUCES_TOTAL          INT(11) DEFAULT 0,
    REDUCES_COMPLETED      INT(11) DEFAULT 0,
    FAILED_MAP_ATTEMPTS    INT(11) DEFAULT 0,
    KILLED_MAP_ATTEMPTS    INT(11) DEFAULT 0,
    FAILED_REDUCE_ATTEMPTS INT(11) DEFAULT 0,
    KILLED_REDUCE_ATTEMPTS INT(11) DEFAULT 0,
    AVG_MAP_TIME           INT(11) DEFAULT 0,
    AVG_SHUFFLE_TIME       INT(11) DEFAULT 0,
    AVG_MERGE_TIME         INT(11) DEFAULT 0,
    AVG_REDUCE_TIME        INT(11) DEFAULT 0,
    SUBMIT_TIME            DATETIME,
    START_TIME             DATETIME,
    FINISH_TIME            DATETIME,
    COUNTERS               LONGTEXT DEFAULT NULL,
    CONFIGURATION          LONGTEXT DEFAULT NULL,
    TASKS                  LONGTEXT DEFAULT NULL,
    REGISTERED_DATE        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (ID)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX IDX_FL_CL_MR_DUMP_01 ON flamingo2.FL_CL_MR_DUMP (JOB_ID);
--
-- Resource Manager Cluster Metrics Monitoring
--

CREATE TABLE IF NOT EXISTS flamingo2.FL_CL_CLUSTER_METRICS (
    id                    INT(11) NOT NULL AUTO_INCREMENT,
    system                VARCHAR(255) DEFAULT NULL,
    name                  VARCHAR(255) DEFAULT NULL,
    type                  VARCHAR(255) DEFAULT NULL,
    totalMemorySum        INT(11) DEFAULT 0,
    usedMemorySum         INT(11) DEFAULT 0,
    nodeSum               INT(11) DEFAULT 0,
    totalVCoreSum         INT(11) DEFAULT 0,
    containerSum          INT(11) DEFAULT 0,
    usedVCoreSum          INT(11) DEFAULT 0,
    newNodes              INT(11) DEFAULT 0,
    runningNodes          INT(11) DEFAULT 0,
    unhealthyNodes        INT(11) DEFAULT 0,
    decommisionedNodes    INT(11) DEFAULT 0,
    lostNodes             INT(11) DEFAULT 0,
    rebootedNodes         INT(11) DEFAULT 0,
    reg_dt                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    yyyy                  VARCHAR(10) DEFAULT NULL,
    mm                    VARCHAR(10) DEFAULT NULL,
    dd                    VARCHAR(10) DEFAULT NULL,
    PRIMARY KEY (id)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- HDFS Monitoring
--

CREATE TABLE IF NOT EXISTS flamingo2.FL_CL_HDFS (
    id                          INT(11) NOT NULL AUTO_INCREMENT,
    system                      VARCHAR(255) DEFAULT NULL,
    name                        VARCHAR(255) DEFAULT NULL,
    type                        VARCHAR(255) DEFAULT NULL,
    nodeAll                     INT(11) DEFAULT NULL,
    nodeDead                    INT(11) DEFAULT NULL,
    nodeLive                    INT(11) DEFAULT NULL,
    nodeDecommisioning          INT(11) DEFAULT NULL,
    blocksTotal                 BIGINT DEFAULT NULL,
    corrupt                     INT(11) DEFAULT NULL,
    underReplicatedBlocks       INT(11) DEFAULT NULL,
    corruptReplicaBlocks        INT(11) DEFAULT NULL,
    pendingReplicationBlocks    INT(11) DEFAULT NULL,
    scheduledReplicationBlocks  INT(11) DEFAULT NULL,
    missingBlocks               INT(11) DEFAULT NULL,
    totalFiles                  BIGINT DEFAULT NULL,
    totalBlocks                 BIGINT DEFAULT NULL,
    totalLoad                   BIGINT DEFAULT NULL,
    capacityRemaining           BIGINT DEFAULT NULL,
    capacityRemainingPercent    FLOAT(7,4) DEFAULT NULL,
    capacityTotal               BIGINT DEFAULT NULL,
    capacityUsed                BIGINT DEFAULT NULL,
    capacityUsedNonDFS          BIGINT DEFAULT NULL,
    capacityUsedPercent         FLOAT(7,4) DEFAULT NULL,
    editLogSize                 BIGINT DEFAULT NULL,
    free                        BIGINT DEFAULT NULL,
    used                        BIGINT DEFAULT NULL,
    total                       BIGINT DEFAULT NULL,
    threads                     INT(11) DEFAULT NULL,
    jvmMaxMemory                INT(11) DEFAULT NULL,
    jvmTotalMemory              INT(11) DEFAULT NULL,
    jvmFreeMemory               INT(11) DEFAULT NULL,
    jvmUsedMemory               INT(11) DEFAULT NULL,
    reg_dt                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    yyyy                        VARCHAR(10) DEFAULT NULL,
    mm                          VARCHAR(10) DEFAULT NULL,
    dd                          VARCHAR(10) DEFAULT NULL,
    PRIMARY KEY (id)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- MapR CLDB Monitoring
--

CREATE TABLE IF NOT EXISTS flamingo2.FL_CL_CLDB (
    id                          INT(11) NOT NULL AUTO_INCREMENT,
    system                      VARCHAR(255) DEFAULT NULL,
    name                        VARCHAR(255) DEFAULT NULL,
    type                        VARCHAR(255) DEFAULT NULL,
    fileServerCount             INT(11) DEFAULT NULL,
    volumeCount                 INT(11) DEFAULT NULL,
    replNumContainerCopied      INT(11) DEFAULT NULL,
    replNumMBCopied             BIGINT DEFAULT NULL,
    replSerializedSize          INT(11) DEFAULT NULL,
    used                        BIGINT DEFAULT NULL,
    free                        BIGINT DEFAULT NULL,
    total                       BIGINT DEFAULT NULL,
    totalFiles                  BIGINT DEFAULT NULL,
    jvmMaxMemory                INT(11) DEFAULT NULL,
    jvmTotalMemory              INT(11) DEFAULT NULL,
    jvmFreeMemory               INT(11) DEFAULT NULL,
    jvmUsedMemory               INT(11) DEFAULT NULL,
    reg_dt                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    yyyy                        VARCHAR(10) DEFAULT NULL,
    mm                          VARCHAR(10) DEFAULT NULL,
    dd                          VARCHAR(10) DEFAULT NULL,
    PRIMARY KEY (id)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Engine, Scheduler Monitoring
--

CREATE TABLE IF NOT EXISTS flamingo2.FL_CL_ENGINE (
    id                          INT(11) NOT NULL AUTO_INCREMENT,
    system                      VARCHAR(255) DEFAULT NULL,
    name                        VARCHAR(255) DEFAULT NULL,
    running                     INT(11) DEFAULT NULL,
    total                       INT(11) DEFAULT NULL,
    jvmMaxMemory                INT(11) DEFAULT NULL,
    jvmTotalMemory              INT(11) DEFAULT NULL,
    jvmFreeMemory               INT(11) DEFAULT NULL,
    jvmUsedMemory               INT(11) DEFAULT NULL,
    reg_dt                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    yyyy                        VARCHAR(10) DEFAULT NULL,
    mm                          VARCHAR(10) DEFAULT NULL,
    dd                          VARCHAR(10) DEFAULT NULL,
    PRIMARY KEY (id)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- System Metrics Monitoring
--

CREATE TABLE IF NOT EXISTS flamingo2.FL_CL_SYS_METRICS (
    id                    INT(11) NOT NULL AUTO_INCREMENT,
    system                VARCHAR(255) DEFAULT NULL,
    name                  VARCHAR(255) DEFAULT NULL,
    type                  VARCHAR(255) DEFAULT NULL,
    hostname              VARCHAR(255) DEFAULT NULL,
    heapMax               BIGINT DEFAULT NULL,
    heapUsed              BIGINT DEFAULT NULL,
    heapTotal             BIGINT DEFAULT NULL,
    heapFree              BIGINT DEFAULT NULL,
    cpuSys                BIGINT DEFAULT NULL,
    cpuIdle               BIGINT DEFAULT NULL,
    cpuUser               BIGINT DEFAULT NULL,
    procCpuUser           BIGINT DEFAULT NULL,
    procCpuSys            BIGINT DEFAULT NULL,
    procCpuTotal          BIGINT DEFAULT NULL,
    procCpuPer            FLOAT(7,4) DEFAULT NULL,
    reg_dt                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    yyyy                  VARCHAR(10) DEFAULT NULL,
    mm                    VARCHAR(10) DEFAULT NULL,
    dd                    VARCHAR(10) DEFAULT NULL,
    PRIMARY KEY (id)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS flamingo2.FL_COLLECTOR_STATUS (
    ID               INT(11) NOT NULL AUTO_INCREMENT,
    SYSTEM           VARCHAR(255) DEFAULT NULL,
    RESOURCE         VARCHAR(255) DEFAULT NULL,
    TYPE             VARCHAR(255) DEFAULT NULL,
    VALUE            VARCHAR(255) DEFAULT NULL,
    LAST_DATE        DATETIME DEFAULT NULL,
    WORK_DATE        DATETIME DEFAULT NULL,
    PRIMARY KEY (ID),
    UNIQUE KEY (SYSTEM, RESOURCE, TYPE)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Flamingo2 Workflow History
--

CREATE TABLE IF NOT EXISTS flamingo2.FL_WORKFLOW_HISTORY (
  ID              INT(11)      NOT NULL AUTO_INCREMENT COMMENT 'Sequence',
  WORKFLOW_ID     VARCHAR(60)  NOT NULL COMMENT 'Workflow String ID',
  JOB_ID          INT(11)      NOT NULL COMMENT 'Job ID',
  JOB_ID_STRING   VARCHAR(60)  NOT NULL COMMENT 'Job String ID',
  WORKFLOW_NAME   VARCHAR(250) NOT NULL COMMENT 'Workflow Name',
  CURRENT_ACTION  VARCHAR(250) DEFAULT '' COMMENT 'Current Action',
  JOB_NAME        VARCHAR(250) NOT NULL COMMENT 'Workflow Name',
  WORKFLOW_XML    LONGTEXT         NOT NULL COMMENT 'Workflow XML',
  VARIABLE        LONGTEXT         DEFAULT NULL COMMENT 'Workflow Variable',
  START_DATE      DATETIME     COMMENT 'Start Date',
  END_DATE        DATETIME     COMMENT 'End Date',
  CAUSE           VARCHAR(250) DEFAULT '' COMMENT 'cause',
  CURRENT_STEP    INTEGER      DEFAULT NULL COMMENT 'Current Step',
  TOTAL_STEP      INTEGER      DEFAULT NULL COMMENT 'Total Step',
  ELAPSED         INTEGER      DEFAULT NULL COMMENT 'Elapsed Time',
  EXCEPTION       LONGTEXT         DEFAULT NULL COMMENT 'Description',
  STATUS          VARCHAR(10)  NOT NULL COMMENT 'Workflow Status',
  USERNAME        VARCHAR(50)  NOT NULL COMMENT 'Writer',
  JOB_TYPE        VARCHAR(20)  NOT NULL COMMENT 'Job Type',
  SF_PARENT_IDENTIFIER VARCHAR(255),
  SF_ROOT_IDENTIFIER VARCHAR(255),
  SF_DEPTH INT(11)    DEFAULT 0,
  SF_TASK_ID VARCHAR(255),
  PRIMARY KEY (ID)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

--
-- Flamingo2 Task History
--

CREATE TABLE IF NOT EXISTS flamingo2.FL_TASK_HISTORY (
  ID                 BIGINT NOT NULL AUTO_INCREMENT,
  IDENTIFIER         VARCHAR(255),
  WID                VARCHAR(255),
  TASK_ID            VARCHAR(255),
  NAME               VARCHAR(255),
  VARS               LONGTEXT,
  USERNAME           VARCHAR(255),
  START_DT           TIMESTAMP,
  END_DT             TIMESTAMP,
  DURATION           INT(11),
  YYYY               VARCHAR(8),
  MM                 VARCHAR(8),
  DD                 VARCHAR(8),
  STATUS             VARCHAR(20),
  LOGDIR             LONGTEXT,
  TREE_ID            INT(11),
  PRIMARY KEY (ID)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS flamingo2.FL_USER_EVENTS
(
  ID                 BIGINT NOT NULL AUTO_INCREMENT,
  NAME               VARCHAR(255),
  REG_DT             TIMESTAMP,
  STATUS             VARCHAR(255),
  MESSAGE            LONGTEXT,
  IS_SEE             BOOLEAN DEFAULT FALSE,
  IDENTIFIER         VARCHAR(255),
  REF_ID             BIGINT,
  USERNAME           VARCHAR(255),
  YYYY               VARCHAR(8),
  MM                 VARCHAR(8),
  DD                 VARCHAR(8),
  PRIMARY KEY (ID),
  UNIQUE KEY (IDENTIFIER)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


CREATE TABLE IF NOT EXISTS flamingo2.FL_BATCH
(
	JOB_ID      VARCHAR(255)  NOT NULL COMMENT 'JOB_ID',
 	JOB_STAT    VARCHAR(20)   COMMENT 'JOB_STAT',
 	WID	        INT           COMMENT 'WORKFLOW TABLE PK',
 	WORKFLOW_ID	VARCHAR(60)   COMMENT 'WORKFLOW_ID',
 	WORKFLOW_NM VARCHAR(250)  COMMENT 'WORKFLOW_NM',
 	JOB_NM	    VARCHAR(250)  COMMENT 'JOB_NM',
 	JOB_VAR	    LONGTEXT      COMMENT 'JOB_VAR',
 	CRON        VARCHAR(20)   COMMENT 'CRON',
 	REG_ID      BIGINT        COMMENT 'User id',
 	REG_DT	    DATETIME      COMMENT 'REG_DT',
 	UPD_DT	    DATETIME      COMMENT 'UPD_DT',
 	PRIMARY KEY (JOB_ID)
 ) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS flamingo2.FL_IOT_SV_METADATA
(
	id                  BIGINT          NOT NULL AUTO_INCREMENT,
 	dataType            VARCHAR(40)     NOT NULL COMMENT 'Data Type: metadata',
 	serviceId	        VARCHAR(40)     NOT NULL COMMENT 'Service ID: 10000001',
 	serviceName	        VARCHAR(40)     NOT NULL COMMENT 'Service Name',
 	serviceTypeId	    VARCHAR(40)     NOT NULL COMMENT 'Service Type ID',
 	serviceTypeName     VARCHAR(100)    NOT NULL COMMENT 'Service Type Name',
 	deviceTypeId	    VARCHAR(100)    NOT NULL COMMENT 'Device Type ID',
 	deviceTypeName	    VARCHAR(100)    NOT NULL COMMENT 'Device Type Name',
 	columnsType	        VARCHAR(40)     NOT NULL COMMENT 'data/custom',
 	columnName	        VARCHAR(100)    COMMENT 'Data/Custom Column Name',
 	columnType	        VARCHAR(50)     COMMENT 'Data/Custom Column Type',
 	columnFiltering	    BOOLEAN         DEFAULT FALSE COMMENT 'Data/Custom Column Filtering',
 	columnMasking       BOOLEAN         DEFAULT FALSE COMMENT 'Data/Custom Column Masking',
 	workDate            DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT 'Working Date: YYYY-MM-DD HH:MM:SS.fffffffff',
 	orderby             INT             DEFAULT 1 COMMENT 'ASC / DESC',
 	PRIMARY KEY (id)
 ) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE INDEX IDX_FL_IOT_SV_METADATA_01 ON flamingo2.FL_IOT_SV_METADATA (id);

CREATE TABLE IF NOT EXISTS flamingo2.FL_IOT (
  id                BIGINT NOT NULL AUTO_INCREMENT,
  serviceId         VARCHAR(255) DEFAULT NULL COMMENT 'Service ID: 10000001',
  serviceName       VARCHAR(255) DEFAULT NULL COMMENT 'Service Name',
  serviceTypeId     VARCHAR(255) DEFAULT NULL COMMENT 'Service Type ID',
  serviceTypeName   VARCHAR(255) DEFAULT NULL COMMENT 'Service Type Name',
  deviceTypeId      VARCHAR(255) DEFAULT NULL COMMENT 'Device Type ID',
  deviceTypeName    VARCHAR(255) DEFAULT NULL COMMENT 'Device Type Name',
  columnsType       VARCHAR(255) DEFAULT NULL COMMENT 'data/custom',
  columnName        VARCHAR(255) DEFAULT NULL COMMENT 'Data/Custom Column Name',
  columnType        VARCHAR(255) DEFAULT NULL COMMENT 'Data/Custom Column Type',
  filtering         TINYINT(1) DEFAULT 0 COMMENT 'Data/Custom Column Filtering',
  masking           TINYINT(1) DEFAULT 0 COMMENT 'Data/Custom Column Masking',
  orderby           INT(11) DEFAULT 1 COMMENT 'ASC / DESC',
  workDate          TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Working Date: YYYY-MM-DD HH:MM:SS.fffffffff',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS flamingo2.FL_SPARK_STREAMING
(
	ID                          BIGINT          NOT NULL AUTO_INCREMENT,
 	SERVER                      VARCHAR(255)    NOT NULL COMMENT 'Server Location',
 	SPARK_USER_WORKING_PATH	    VARCHAR(255)    NOT NULL COMMENT 'Flamingo User Spark Working Path',
 	JAR_FILE_FQP	            VARCHAR(255)    NOT NULL COMMENT 'Fully Qualified Path Uploaded Jar File',
 	JAR_FILENAME	            VARCHAR(255)    NOT NULL COMMENT 'Jar Filename',
 	APPLICATION_ID	            VARCHAR(255)    NOT NULL COMMENT 'Application ID',
 	APPLICATION_NAME	        VARCHAR(255)    NOT NULL COMMENT 'Application Name',
 	STREAMING_CLASS	            VARCHAR(255)    NOT NULL COMMENT 'Spark Streaming Class',
 	JAVA_OPTS	                VARCHAR(255)    NOT NULL COMMENT 'Java Command Line Options',
 	VARIABLES	                VARCHAR(255)    COMMENT 'Java Command Line Parameters',
 	USERNAME                    VARCHAR(255)    NOT NULL COMMENT 'Username',
 	STATE	                    VARCHAR(255)    DEFAULT 'STANDBY' COMMENT 'Application State',
 	REGISTERED_DATE             TIMESTAMP       DEFAULT CURRENT_TIMESTAMP COMMENT 'Registered Date',
 	START_TIME	                DATETIME        COMMENT 'Start Time',
 	FINISH_TIME	                DATETIME        COMMENT 'Finish Time',
 	DURATION	                DATETIME        COMMENT 'Duration',
 	PRIMARY KEY (ID),
 	UNIQUE KEY (APPLICATION_ID)
 ) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

  CREATE TABLE IF NOT EXISTS flamingo2.FL_ESPER
(
	ID                          BIGINT          NOT NULL AUTO_INCREMENT,
 	SERVER                      VARCHAR(255)    NOT NULL COMMENT 'Server Location',
 	SPARK_USER_WORKING_PATH	    VARCHAR(255)    NOT NULL COMMENT 'Flamingo User Spark Working Path',
 	JAR_FILE_FQP	            VARCHAR(255)    NOT NULL COMMENT 'Fully Qualified Path Uploaded Jar File',
 	JAR_FILENAME	            VARCHAR(255)    NOT NULL COMMENT 'Jar Filename',
 	APPLICATION_ID	            VARCHAR(255)    NOT NULL COMMENT 'Application ID',
 	APPLICATION_NAME	        VARCHAR(255)    NOT NULL COMMENT 'Application Name',
 	STREAMING_CLASS	            VARCHAR(255)    NOT NULL COMMENT 'Spark Streaming Class',
 	JAVA_OPTS	                VARCHAR(255)    NOT NULL COMMENT 'Java Command Line Options',
 	VARIABLES	                VARCHAR(255)    COMMENT 'Java Command Line Parameters',
 	USERNAME                    VARCHAR(255)    NOT NULL COMMENT 'Username',
 	STATE	                    VARCHAR(255)    DEFAULT 'STANDBY' COMMENT 'Application State',
 	REGISTERED_DATE             TIMESTAMP       DEFAULT CURRENT_TIMESTAMP COMMENT 'Registered Date',
 	START_TIME	                DATETIME        COMMENT 'Start Time',
 	FINISH_TIME	                DATETIME        COMMENT 'Finish Time',
 	DURATION	                DATETIME        COMMENT 'Duration',
 	PRIMARY KEY (ID),
 	UNIQUE KEY (APPLICATION_ID)
 ) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS flamingo2.FL_SPARK_STREAMING_HISTORY
(
	ID                          BIGINT          NOT NULL AUTO_INCREMENT,
 	APPLICATION_ID	            VARCHAR(255)    NOT NULL COMMENT 'Application ID',
 	APPLICATION_NAME	        VARCHAR(255)    NOT NULL COMMENT 'Application Name',
 	USERNAME                    VARCHAR(255)    NOT NULL COMMENT 'Username',
 	CPU_USAGE                   DOUBLE(3,1)     NOT NULL COMMENT 'CPU Usage',
 	ACTIVE_THREADS              BIGINT          NOT NULL COMMENT 'Active Threads',
 	MEMORY_USAGE                BIGINT          NOT NULL COMMENT 'Memory Usage',
 	REGISTERED_DATE             TIMESTAMP       DEFAULT CURRENT_TIMESTAMP COMMENT 'Registered Date',
 	PRIMARY KEY (ID)
 ) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS flamingo2.FL_SP_APPS
(
 	 APPID	    VARCHAR(100)    NOT NULL COMMENT 'APPID',
 	 APPNAME	  VARCHAR(1000)   NOT NULL COMMENT 'APPNAME',
 	 STARTED	  TIMESTAMP       NOT NULL COMMENT 'STARTED',
 	 COMPLETED  TIMESTAMP       NOT NULL COMMENT 'COMPLETED',
 	 USER	      VARCHAR(50)     NOT NULL COMMENT 'USER',
 	 LOCATION	  VARCHAR(1000)   COMMENT 'LOCATION'
 ) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE flamingo2.FL_SP_APPS ADD CONSTRAINT FL_SP_APPS_PK PRIMARY KEY ( APPID );

CREATE TABLE IF NOT EXISTS flamingo2.FL_SP_JOBS
(
 	 APPID	           VARCHAR(100)    NOT NULL COMMENT 'APPID',
 	 JOBID	           INT   NOT NULL COMMENT 'JOBID',
 	 JOBNAME	         VARCHAR(1000) COMMENT 'JOBNAME',
 	 SUBMITTED	       TIMESTAMP   NOT NULL COMMENT 'SUBMITTED',
 	 COMPLETED	       TIMESTAMP   NOT NULL COMMENT 'COMPLETED',
 	 STAGES	           INT 	 DEFAULT 0  COMMENT 'STAGES',
 	 STAGE_COMPLETED	 INT 	 DEFAULT 0  COMMENT 'STAGE_COMPLETED',
 	 STAGE_SKIPPED	   INT 	 DEFAULT 0  COMMENT 'STAGE_SKIPPED',
 	 STAGE_FAILED	     INT 	 DEFAULT 0  COMMENT 'STAGE_FAILED',
 	 TASKS	           INT 	 DEFAULT 0  COMMENT 'TASKS',
 	 TASK_COMPLETED	   INT 	 DEFAULT 0  COMMENT 'TASK_COMPLETED',
 	 TASK_SKIPPED	     INT 	 DEFAULT 0  COMMENT 'TASK_SKIPPED',
 	 TASK_FAILED	     INT 	 DEFAULT 0  COMMENT 'TASK_FAILED',
 	 DESCRIPTION	     TEXT  COMMENT 'DESCRIPTION',
 	 STATUS	           VARCHAR(20)   COMMENT 'STATUS'
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE flamingo2.FL_SP_JOBS ADD CONSTRAINT FL_SP_JOBS_PK PRIMARY KEY ( APPID,JOBID );

CREATE TABLE IF NOT EXISTS flamingo2.FL_SP_STAGES
(
 	 APPID                	 VARCHAR(100)    NOT NULL COMMENT 'APPID',
 	 JOBID	                 INT   NOT NULL COMMENT 'JOBID',
 	 STAGEID	               INT   NOT NULL COMMENT 'STAGEID',
 	 ATTEMPTID	             INT   NOT NULL COMMENT 'ATTEMPTID',
 	 SUBMITTED	             TIMESTAMP  COMMENT 'SUBMITTED',
 	 COMPLETED	             TIMESTAMP  COMMENT 'COMPLETED',
 	 NAME	                   VARCHAR(1000)   COMMENT 'NAME',
 	 DETAILS	               MEDIUMTEXT  COMMENT 'DETAILS',
 	 TASKS	                 INT   NOT NULL COMMENT 'TASKS',
 	 TASK_COMPLETE	         INT   NOT NULL COMMENT 'TASK_COMPLETE',
 	 TASK_FAILED	           INT   NOT NULL COMMENT 'TASK_FAILED',
 	 INPUT_BYTES	           BIGINT   NOT NULL COMMENT 'INPUT_BYTES',
 	 INPUT_RECORDS	         BIGINT   NOT NULL COMMENT 'INPUT_RECORDS',
 	 OUTPUT_BYTES	           BIGINT   NOT NULL COMMENT 'OUTPUT_BYTES',
 	 OUTPUT_RECORDS	         BIGINT   NOT NULL COMMENT 'OUTPUT_RECORDS',
 	 SHUFFLE_READ_BYTES	     BIGINT   NOT NULL COMMENT 'SHUFFLE_READ_BYTES',
 	 SHUFFLE_READ_RECORDS	   BIGINT   NOT NULL COMMENT 'SHUFFLE_READ_RECORDS',
 	 SHUFFLE_WRITE_BYTES	   BIGINT   NOT NULL COMMENT 'SHUFFLE_WRITE_BYTES',
 	 SHUFFLE_WRITE_RECRODS	 BIGINT   NOT NULL COMMENT 'SHUFFLE_WRITE_RECRODS',
 	 STATUS	                 VARCHAR(20)    NOT NULL COMMENT 'STATUS'
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


ALTER TABLE flamingo2.FL_SP_STAGES ADD CONSTRAINT FL_SP_STAGES_PK PRIMARY KEY ( APPID,JOBID,STAGEID,ATTEMPTID );

CREATE TABLE IF NOT EXISTS flamingo2.FL_SP_STAGE_DETAIL
(
 	 APPID	     VARCHAR(100)    NOT NULL COMMENT 'APPID',
 	 JOBID	     INT   NOT NULL COMMENT 'JOBID',
 	 STAGEID	   INT   NOT NULL COMMENT 'STAGEID',
 	 ATTEMPTID	 INT   NOT NULL COMMENT 'ATTEMPTID',
 	 TYPE	       VARCHAR(50)    NOT NULL COMMENT 'TYPE',
 	 JSON	       TEXT   NOT NULL COMMENT 'JSON'
 ) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE flamingo2.FL_SP_STAGE_DETAIL ADD CONSTRAINT FL_SP_STAGE_DETAIL_PK PRIMARY KEY ( APPID,JOBID,STAGEID,ATTEMPTID,TYPE );

CREATE TABLE IF NOT EXISTS flamingo2.FL_SP_EXECUTORS
(
 	 APPID	               VARCHAR(100)    NOT NULL COMMENT 'APPID',
 	 EXECUTORID	           VARCHAR(100)    NOT NULL COMMENT 'EXECUTORID',
 	 ADDRESS	             VARCHAR(20)    NOT NULL COMMENT 'ADDRESS',
 	 PORT	                 INT  COMMENT 'PORT',
 	 RDD_BLOCKS	           BIGINT 	 DEFAULT 0 COMMENT 'RDD_BLOCKS',
 	 MAX_MEMORY	           BIGINT 	 DEFAULT 0 COMMENT 'MAX_MEMORY',
 	 MEMORY_USED	         BIGINT 	 DEFAULT 0 COMMENT 'MEMORY_USED',
 	 DISK_USED	           BIGINT 	 DEFAULT 0 COMMENT 'DISK_USED',
 	 ACTIVE_TASKS	         BIGINT 	 DEFAULT 0 COMMENT 'ACTIVE_TASKS',
 	 FAILED_TASKS	         BIGINT 	 DEFAULT 0 COMMENT 'FAILED_TASKS',
 	 COMPLETED_TASKS	     BIGINT 	 DEFAULT 0 COMMENT 'COMPLETED_TASKS',
 	 TOTAL_TASKS	         BIGINT 	 DEFAULT 0 COMMENT 'TOTAL_TASKS',
 	 TOTAL_DURATION	       BIGINT 	 DEFAULT 0 COMMENT 'TOTAL_DURATION',
 	 TOTAL_INPUT_BYTES	   BIGINT 	 DEFAULT 0 COMMENT 'TOTAL_INPUT_BYTES',
 	 TOTAL_SHUFFLE_READ	   BIGINT 	 DEFAULT 0 COMMENT 'TOTAL_SHUFFLE_READ',
 	 TOTAL_SHUFFLE_WRITE	 BIGINT 	 DEFAULT 0 COMMENT 'TOTAL_SHUFFLE_WRITE',
 	 TOTAL_CORES	         INT 	 DEFAULT 0 COMMENT 'TOTAL_CORES',
 	 STDOUT_URL	           VARCHAR(1000)  COMMENT 'STDOUT_URL',
 	 STDERR_URL	           VARCHAR(1000)  COMMENT 'STDERR_URL'
 ) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE flamingo2.FL_SP_EXECUTORS ADD CONSTRAINT FL_SP_EXECUTORS_PK PRIMARY KEY ( APPID,EXECUTORID );

CREATE TABLE IF NOT EXISTS flamingo2.FL_SP_STORAGE
(
 	 APPID                     VARCHAR(100) NOT NULL COMMENT 'APPID',
 	 RDDID	                   INT   NOT NULL COMMENT 'RDDID',
 	 RDD_NAME	                 TEXT    NOT NULL COMMENT 'RDD_NAME',
 	 CACHED_PARTITIONS	       INT  COMMENT 'CACHED_PARTITIONS',
 	 PARTITIONS	               INT  COMMENT 'PARTITIONS',
 	 FRACTION_CACHED	         FLOAT COMMENT 'FRACTION_CACHED',
 	 MEMORY                    BIGINT  COMMENT 'MEMORY',
 	 EXTERNAL_BLOCK_STORE	     BIGINT COMMENT 'EXTERNAL_BLOCK_STORE',
 	 DISK	                     BIGINT  COMMENT 'DISK',
 	 USE_DISK	                 TINYINT COMMENT 'USE_DISK',
 	 USE_MEMORY	               TINYINT COMMENT 'USE_MEMORY',
 	 USE_EXTERNAL_BLOCK_STORE	 TINYINT COMMENT 'USE_EXTERNAL_BLOCK_STORE',
 	 DESERIALIZAED	           TINYINT COMMENT 'DESERIALIZAED',
 	 REPLICATION	             INT COMMENT 'REPLICATION'
 ) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE flamingo2.FL_SP_STORAGE ADD CONSTRAINT FL_SP_STORAGE_PK PRIMARY KEY ( APPID,RDDID );

CREATE TABLE IF NOT EXISTS flamingo2.FL_SP_TIMELINE
(
 	 APPID	     VARCHAR(100)    NOT NULL COMMENT 'APPID',
 	 JOBID	     INT   NOT NULL COMMENT 'JOBID',
 	 STAGEID	   INT   NOT NULL COMMENT 'STAGEID',
 	 ATTEMPTID	 INT   NOT NULL COMMENT 'ATTEMPTID',
 	 TYPE	       VARCHAR(20)    NOT NULL COMMENT 'TYPE',
 	 JSON	       MEDIUMTEXT  COMMENT 'JSON'
 ) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE flamingo2.FL_SP_TIMELINE ADD CONSTRAINT FL_SP_TIMELINE_PK PRIMARY KEY ( APPID,JOBID,STAGEID,ATTEMPTID,TYPE );

CREATE
    ALGORITHM = UNDEFINED
    SQL SECURITY DEFINER
VIEW generator_16 AS
    SELECT 0 AS n
    UNION ALL SELECT 1 AS 1
    UNION ALL SELECT 2 AS 2
    UNION ALL SELECT 3 AS 3
    UNION ALL SELECT 4 AS 4
    UNION ALL SELECT 5 AS 5
    UNION ALL SELECT 6 AS 6
    UNION ALL SELECT 7 AS 7
    UNION ALL SELECT 8 AS 8
    UNION ALL SELECT 9 AS 9
    UNION ALL SELECT 10 AS 10
    UNION ALL SELECT 11 AS 11
    UNION ALL SELECT 12 AS 12
    UNION ALL SELECT 13 AS 13
    UNION ALL SELECT 14 AS 14
    UNION ALL SELECT 15 AS 15;

CREATE
    ALGORITHM = UNDEFINED
    SQL SECURITY DEFINER
VIEW generator_256 AS
    SELECT
        ((hi.n * 16) + lo.n) AS n
    FROM
        (generator_16 hi
        JOIN generator_16 lo);


CREATE TABLE IF NOT EXISTS flamingo2.EXO_LOGGING (
 	id	                BIGINT          NOT NULL AUTO_INCREMENT,
 	processId	        VARCHAR(255)    NOT NULL COMMENT 'Process ID',
 	processType	        VARCHAR(255)    NOT NULL COMMENT 'Process Type',
 	loggingLevel	    VARCHAR(255)    NOT NULL COMMENT 'Logging Level',
 	collectionReader	VARCHAR(255) 	NOT NULL COMMENT 'Collection Reader',
 	ip	                VARCHAR(255) 	NOT NULL COMMENT 'IP',
 	annotatorType	    VARCHAR(255) 	NOT NULL COMMENT 'Annotator Type',
 	data	            LONGTEXT 	    NOT NULL COMMENT 'Data',
 	logDate	            DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT 'Log Date',
 	PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;
