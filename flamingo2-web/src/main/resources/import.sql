CREATE DATABASE flamingo2 CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI;
CREATE USER 'flamingo'@'localhost' IDENTIFIED BY 'flamingo';
GRANT ALL PRIVILEGES ON *.* TO 'flamingo'@'localhost';
FLUSH PRIVILEGES;

USE flamingo2;

CREATE TABLE IF NOT EXISTS flamingo2.FL_AUTHORITIES (
  ID              BIGINT NOT NULL AUTO_INCREMENT,
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
  REG_DT          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UPD_DT          TIMESTAMP DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (ID)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

INSERT INTO flamingo2.FL_ORG (ID, ORG_CD, ORG_NM, DESCRIPTION, UPD_DT)
VALUES (1, 'OCE', 'Cloudine', 'Cloudine', CURRENT_TIMESTAMP);

INSERT INTO flamingo2.FL_ORG (ID, ORG_CD, ORG_NM, DESCRIPTION, UPD_DT)
VALUES (2, 'OCE', 'Demo', '데모', CURRENT_TIMESTAMP);

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
  ID                BIGINT NOT NULL AUTO_INCREMENT,
  USER_NM           VARCHAR(255),
  PASSWD            VARCHAR(255),
  EMAIL             VARCHAR(255),
  NM                VARCHAR(255),
  DESCRIPTION       LONGTEXT,
  LINUX_USER_HOME   VARCHAR(255),
  HDFS_USER_HOME    VARCHAR(255),
  USER_GROUP        VARCHAR(255),
  REG_DT            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UPD_DT            TIMESTAMP DEFAULT '0000-00-00 00:00:00',
  ENABLED           BOOLEAN NOT NULL DEFAULT FALSE,
  ORG_ID            BIGINT NOT NULL DEFAULT 1,
  AUTH_ID           BIGINT NOT NULL DEFAULT 2,
  LEVEL             SMALLINT NOT NULL DEFAULT 5,
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

CREATE TABLE IF NOT EXISTS flamingo2.FL_USER_AUTH (
  USER_ID              BIGINT NOT NULL,
  AUTH_ID              BIGINT NOT NULL DEFAULT 2,
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
  ID                    BIGINT        NOT NULL  AUTO_INCREMENT,
  HDFS_PATH_PATTERN     VARCHAR(255)  NOT NULL  COMMENT 'HDFS Path Pattern',
  CREATE_DIR   			BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Create Directory',
  COPY_DIR     			BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Copy Directory',
  MOVE_DIR     			BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Move Directory',
  RENAME_DIR  			BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Rename Directory',
  DELETE_DIR    		BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Delete Directory',
  MERGE_DIR    		    BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Merge All Files In Directory',
  PERMISSION_DIR    	BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Set Permission To Directories And Files',
  CREATE_DB_DIR     	BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Create Hive Database',
  CREATE_TABLE_DIR  	BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Create Hive Table',
  COPY_FILE     		BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Copy File',
  MOVE_FILE     		BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Move File',
  RENAME_FILE  			BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Rename File',
  DELETE_FILE    		BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Delete File',
  UPLOAD_FILE    		BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Upload File',
  DOWNLOAD_FILE     	BOOLEAN       NOT NULL  DEFAULT FALSE COMMENT 'Download File',
  VIEW_FILE  	        BOOLEAN		  NOT NULL  DEFAULT FALSE COMMENT 'View Contents Into A File',
  PERMISSION_FILE  	    BOOLEAN		  NOT NULL  DEFAULT FALSE COMMENT 'Set Permission To Files',
  REG_DT                TIMESTAMP     NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT 'Registered Date',
  UPD_DT                TIMESTAMP     NOT NULL  DEFAULT '0000-00-00 00:00:00' COMMENT 'Updated Date',
  AUTH_ID				BIGINT        NOT NULL  DEFAULT 2 COMMENT 'Authority ID',
  LEVEL                 SMALLINT	  NOT NULL  DEFAULT 5 COMMENT 'User Level',
  FOREIGN KEY (AUTH_ID) REFERENCES FL_AUTHORITIES (ID),
  FOREIGN KEY (LEVEL) REFERENCES FL_USER_LEVEL (LEVEL),
  PRIMARY KEY (ID),
  UNIQUE KEY (HDFS_PATH_PATTERN, AUTH_ID, LEVEL)
) ENGINE =InnoDB
  DEFAULT CHARSET = UTF8;

INSERT INTO flamingo2.FL_HDFS_PATH_AUTH (
  ID,
  HDFS_PATH_PATTERN,
  CREATE_DIR, COPY_DIR, MOVE_DIR, RENAME_DIR, DELETE_DIR, MERGE_DIR, PERMISSION_DIR, CREATE_DB_DIR, CREATE_TABLE_DIR,
  COPY_FILE, MOVE_FILE, RENAME_FILE, DELETE_FILE, UPLOAD_FILE, DOWNLOAD_FILE, VIEW_FILE, PERMISSION_FILE,
  UPD_DT, AUTH_ID, LEVEL)
VALUES (1, '/**', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 1, 1);

INSERT INTO flamingo2.FL_HDFS_PATH_AUTH (
  ID,
  HDFS_PATH_PATTERN,
  CREATE_DIR, COPY_DIR, MOVE_DIR, RENAME_DIR, DELETE_DIR, MERGE_DIR, PERMISSION_DIR, CREATE_DB_DIR, CREATE_TABLE_DIR,
  COPY_FILE, MOVE_FILE, RENAME_FILE, DELETE_FILE, UPLOAD_FILE, DOWNLOAD_FILE, VIEW_FILE, PERMISSION_FILE,
  UPD_DT, AUTH_ID, LEVEL)
VALUES (2, '/user/cloudine/**', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 2, 2);

INSERT INTO flamingo2.FL_HDFS_PATH_AUTH (
  ID,
  HDFS_PATH_PATTERN,
  CREATE_DIR, COPY_DIR, MOVE_DIR, RENAME_DIR, DELETE_DIR, MERGE_DIR, PERMISSION_DIR, CREATE_DB_DIR, CREATE_TABLE_DIR,
  COPY_FILE, MOVE_FILE, RENAME_FILE, DELETE_FILE, UPLOAD_FILE, DOWNLOAD_FILE, VIEW_FILE, PERMISSION_FILE,
  UPD_DT, AUTH_ID, LEVEL)
VALUES (3, '/user/demo/**', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, CURRENT_TIMESTAMP, 2, 2);

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
('MN003','워크플로우','designer','TOP',3,'Y','menu-workflow','워크플로우','Workflow','Workflow','Workflow'),
('MN003001','워크플로우 디자이너','designer.Designer','MN003',3,'Y','fa-picture-o','워크플로우 디자이너','Workflow Designer','Workflow Designer','Workflow Designer'),
('MN003002','워크플로우 모니터링','dashboard.Dashboard','MN003',3,'Y','fa-pie-chart','워크플로우 모니터링','Workflow Monitoring','Workflow Monitoring','Workflow Monitoring'),
('MN004','배치 작업 관리','batch.Job','TOP',4,'Y','menu-batch','배치 작업 관리','Batch Job Management','Batch Job Management','Batch Job Management'),
('MN005','파일 시스템 관리','fs','TOP',5,'Y','menu-filesystem','파일 시스템 관리','File System Management','File System Management','File System Management'),
('MN005001','HDFS 브라우저','fs.hdfs.Browser','MN005',1,'Y','','HDFS 브라우저','HDFS Browser','HDFS Browser','HDFS Browser'),
('MN005002','HDFS Audit 로그','fs.audit.Audit','MN005',2,'Y','','HDFS Audit 로그','HDFS Audit Log','HDFS Audit Log','HDFS Audit Log'),
('MN006','Apache Hive','hive.Hive','TOP',6,'Y','menu-hive','Apache Hive','Apache Hive','Apache Hive','Apache Hive'),
('MN007','Apache Pig','pig.Pig','TOP',7,'Y','menu-pig','Apache Pig','Apache Pig','Apache Pig','Apache Pig'),
('MN008','RStudio','r.RStudio','TOP',8,'Y','menu-rstudio','RStudio','RStudio','RStudio','RStudio'),
('MN009','시각화','visualization.ggplot2.Ggplot2','TOP',9,'Y','menu-visual','시각화','Visualization','Visualization','Visualization'),
('MN010','Pivotal HAWQ','hawq.Hawq','TOP',10,'Y','menu-hawq','Pivotal HAWQ','Pivotal HAWQ','Pivotal HAWQ','Pivotal HAWQ'),
('MN011','아카이브','system','TOP',11,'Y','menu-archive','아카이브','Archive','Archive','Archive'),
('MN011001','YARN 애플리케이션','archive.yarn.YarnApplication','MN011',1,'Y','','YARN 애플리케이션','YARN Application','YARN Application','YARN Application'),
('MN011002','MapReduce','archive.mapreduce.HistoryServer','MN011',2,'Y','','MapReduce','MapReduce','MapReduce','MapReduce'),
('MN012','터미널','terminal.Terminals','TOP',12,'Y','menu-terminal','터미널','Terminal','Terminal','Terminal'),
('MN099','시스템 관리','system','TOP',99,'Y','menu-system','시스템 관리','System Management','System Management','System Management'),
('MN099001','다국어 관리','system.language.Language','MN099',1,'Y','','다국어 관리','Language Management','Language Management','Language Management'),
('MN099002','메뉴 관리','system.menu.Menu','MN099',1,'Y','','메뉴 관리','Menu Management','Menu Management','Menu Management'),
('MN099003','사용자 관리','system.user.User','MN099',1,'Y','','사용자 관리','User Management','User Management','User Management'),
('MN099004','HDFS 브라우저 권한 관리','system.authority.HdfsBrowserAuthority','MN099',1,'Y','','HDFS 브라우저 권한 관리','HDFS Browser Authority','HDFS Browser Authority','HDFS Browser Authority'),
('MN099005','HAWQ 권한 관리','system.hawq.HawqAuth','MN099',1,'Y','','HAWQ 권한 관리','HAWQ Authority','HAWQ Authority','HAWQ Authority'),
('MN099006','라이센스','system.license.License','MN099',1,'Y','','라이센스','License','License','License');

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
    id                      INT(11) NOT NULL AUTO_INCREMENT,
    system                  VARCHAR(255) DEFAULT NULL,
    applicationId           VARCHAR(255) DEFAULT NULL,
    applicationType         VARCHAR(255) DEFAULT NULL,
    progress                VARCHAR(10) DEFAULT NULL,
    queue                   VARCHAR(255) DEFAULT NULL,
    memorySeconds           INT(11) NULL,
    rpcPort                 INT(11) NOT NULL,
    amHost                  VARCHAR(255) DEFAULT NULL,
    usedResourcesMemory     INT(11) NULL,
    startTime               DATETIME,
    reservedResourcesVcores INT(11) NULL,
    reservedResourcesMemory INT(11) NULL,
    trackingUrl             LONGTEXT DEFAULT NULL,
    yarnApplicationState    VARCHAR(255) DEFAULT NULL,
    neededResourcesVcores   INT(11) NULL,
    name                    LONGTEXT DEFAULT NULL,
    numReservedContainers   INT(11) NULL,
    usedResourcesVcores     INT(11) NULL,
    finishTime              DATETIME,
    numUsedContainers       INT(11) NOT NULL,
    finalApplicationStatus  VARCHAR(255) DEFAULT NULL,
    user                    VARCHAR(255) DEFAULT NULL,
    neededResourcesMemory   INT(11) NULL,
    vcoreSeconds            INT(11) NULL,
    diagnostics             LONGTEXT DEFAULT NULL,
    log                     LONGTEXT DEFAULT NULL,
    reg_dt                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
    id                    INT(11) NOT NULL AUTO_INCREMENT,
    system                VARCHAR(255) DEFAULT NULL,
    jobId                 VARCHAR(255) DEFAULT NULL,
    name                  LONGTEXT DEFAULT NULL,
    queue                 VARCHAR(255) DEFAULT NULL,
    user                  VARCHAR(255) DEFAULT NULL,
    state                 VARCHAR(255) DEFAULT NULL,
    username              VARCHAR(255) DEFAULT '',
    type                  VARCHAR(255) DEFAULT 'MAPREDUCE',
    mapsTotal             INT(11),
    mapsCompleted         INT(11),
    reducesTotal          INT(11),
    reducesCompleted      INT(11),
    submitTime            DATETIME,
    startTime             DATETIME,
    finishTime            DATETIME,
    counters              LONGTEXT DEFAULT NULL,
    configuration         LONGTEXT DEFAULT NULL,
    tasks                 LONGTEXT DEFAULT NULL,
    reg_dt                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (ID)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
