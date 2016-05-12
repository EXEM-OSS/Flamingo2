///////////////////////////////////////
// Variable Define
///////////////////////////////////////

var CONSTANTS                           = {};

CONSTANTS.TEMPLATE                      = {};
CONSTANTS.CONTEXT_PATH                  = '';
CONSTANTS.DESIGNER                      = {};
CONSTANTS.PIG                           = {};
CONSTANTS.HIVE                          = {};
CONSTANTS.HIVE.METASTORE                = {};
CONSTANTS.HIVE.QUERY                    = {};
CONSTANTS.COLLECTOR                     = {};
CONSTANTS.ADMIN                         = {};
CONSTANTS.ADMIN.WE                      = {};
CONSTANTS.ADMIN.HADOOP                  = {};
CONSTANTS.ADMIN.HIVE                    = {};
CONSTANTS.ADMIN.USER                    = {};
CONSTANTS.ADMIN.HDFS                    = {};
CONSTANTS.ADMIN.MENU                    = {};
CONSTANTS.ADMIN.AUTH                    = {};
CONSTANTS.FS                            = {};
CONSTANTS.FS.AUDIT                      = {};
CONSTANTS.BATCH                         = {};
CONSTANTS.DASHBOARD                     = {};
CONSTANTS.DASHBOARD.TASK                = {};
CONSTANTS.TREE                          = {};
CONSTANTS.USER                          = {};
CONSTANTS.USER.MANAGE                   = {};
CONSTANTS.USER.QA                       = {};
CONSTANTS.MENU                          = {};
CONSTANTS.MONITORING                    = {};
CONSTANTS.MONITORING.CLUSTERNODES       = {};
CONSTANTS.MONITORING.NAMENODE           = {};
CONSTANTS.MONITORING.HS                 = {};
CONSTANTS.MONITORING.RM                 = {};
CONSTANTS.MONITORING.CLDB               = {};
CONSTANTS.SYSTEM                        = {};
CONSTANTS.SYSTEM.LANGUAGE               = {};
CONSTANTS.SYSTEM.USER                   = {};
CONSTANTS.SYSTEM.MENU                   = {};
CONSTANTS.SYSTEM.AUTHORITY              = {};
CONSTANTS.CONFIG                        = {};
CONSTANTS.HAWQ                          = {};
CONSTANTS.HAWQ.BROWSER                  = {};
CONSTANTS.HAWQ.EDITOR                   = {};
CONSTANTS.HAWQ.AUTH                     = {};
CONSTANTS.VISUAL                        = {};
CONSTANTS.SETTING                       = {};
CONSTANTS.SETTING.META                  = {};
CONSTANTS.EVENTLIST                     = {};
CONSTANTS.SERVICELIST                   = {};
CONSTANTS.PREFERENCES                   = {};

///////////////////////////////////////
// Configuration
///////////////////////////////////////

CONSTANTS.MANUAL_PAGE     = '/resource/manual/index.html';

CONSTANTS.CONFIG.ENGINES  = '/config/engines.json';

///////////////////////////////////////
// System > Language
///////////////////////////////////////

CONSTANTS.SYSTEM.LANGUAGE.GET_ALL                   = '/system/language/languageList.json';
CONSTANTS.SYSTEM.LANGUAGE.EXPORT_XLSX               = '/system/language/export/message.xlsx';
CONSTANTS.SYSTEM.LANGUAGE.EXPORT_ZIP                = '/system/language/export/message.zip';
CONSTANTS.SYSTEM.LANGUAGE.IMPORT_XLSX               = '/system/language/import/message.json';
CONSTANTS.SYSTEM.LANGUAGE.IMPORT_ZIP               = '/system/language/import/zip.json';
CONSTANTS.SYSTEM.LANGUAGE.SAVE                      = '/system/language/save.json';


///////////////////////////////////////
// System > User
///////////////////////////////////////

CONSTANTS.SYSTEM.USER.ADD_ACKNOWLEDGE            = '/system/user/acknowledge.json';
CONSTANTS.SYSTEM.USER.ADD_USER                   = '/system/user/createUser.json';
CONSTANTS.SYSTEM.USER.ADD_ORGANIZATION           = '/system/user/createOrganization.json';
CONSTANTS.SYSTEM.USER.UPDATE_USER_PWD            = '/system/user/updatePassword.json';
CONSTANTS.SYSTEM.USER.UPDATE_USER_INFO           = '/system/user/updateUserInfo.json';
CONSTANTS.SYSTEM.USER.UPDATE_ORG_INFO            = '/system/user/updateOrganizationInfo.json';
CONSTANTS.SYSTEM.USER.DELETE_USER                = '/system/user/deleteUser.json';
CONSTANTS.SYSTEM.USER.DELETE_ORGANIZATION        = '/system/user/deleteOrganization.json';
CONSTANTS.SYSTEM.USER.GET_USER_ALL               = '/system/user/userList.json';
CONSTANTS.SYSTEM.USER.GET_ORGANIZATION_ALL       = '/system/user/organizationList.json';
CONSTANTS.SYSTEM.USER.GET_AUTHORITY_ALL          = '/system/user/authorityList.json';

///////////////////////////////////////
// System > HDFS Browser Authority
///////////////////////////////////////

CONSTANTS.SYSTEM.AUTHORITY.GET_HDFS_BROWSER_AUTH_ALL        = '/system/authority/hdfsBrowserAuthList.json';
CONSTANTS.SYSTEM.AUTHORITY.GET_HDFS_BROWSER_AUTH_DETAIL     = '/system/authority/hdfsBrowserAuthDetail.json';
CONSTANTS.SYSTEM.AUTHORITY.GET_USER_AUTH_ALL                = '/system/authority/userAuthAll.json';
CONSTANTS.SYSTEM.AUTHORITY.GET_USER_LEVEL_ALL               = '/system/authority/userLevelAll.json';
CONSTANTS.SYSTEM.AUTHORITY.ADD_HDFS_BROWSER_AUTH            = '/system/authority/createHdfsBrowserAuth.json';
CONSTANTS.SYSTEM.AUTHORITY.DELETE_HDFS_BROWSER_AUTH         = '/system/authority/deleteHdfsBrowserAuth.json';
CONSTANTS.SYSTEM.AUTHORITY.UPDATE_HDFS_BROWSER_AUTH         = '/system/authority/updateHdfsBrowserAuth.json';

///////////////////////////////////////
// REGISTER USER
///////////////////////////////////////

CONSTANTS.USER.REGISTER  = '/auth/register';

///////////////////////////////////////
// USER LOGOUT
///////////////////////////////////////

CONSTANTS.USER.LOGOUT  = '/auth/logout';

///////////////////////////////////////
// Monitoring
///////////////////////////////////////

// Resource Manager
CONSTANTS.MONITORING.RM.ALL_APPS                      = '/monitoring/resourcemanager/apps/all.json';
CONSTANTS.MONITORING.RM.JVM_HEAP                      = '/monitoring/resourcemanager/jvmheap.json';
CONSTANTS.MONITORING.RM.CONFIGURATION                 = '/monitoring/resourcemanager/configuration.json?clusterName=default';
CONSTANTS.MONITORING.RM.NODESTATUS                    = '/monitoring/resourcemanager/nodestatus.json';
CONSTANTS.MONITORING.RM.APPSTATUS                     = '/monitoring/resourcemanager/appstatus.json';
CONSTANTS.MONITORING.RM.CONTAINERSTATUS               = '/monitoring/resourcemanager/containerstatus.json';
CONSTANTS.MONITORING.RM.QUEUEMEMORY                   = '/monitoring/resourcemanager/queuememory.json';
CONSTANTS.MONITORING.RM.JVMHEAP                       = '/monitoring/resourcemanager/jvmheap.json';
CONSTANTS.MONITORING.RM.APP_RUNNING                   = '/monitoring/resourcemanager/apps/running.json';

// History Server
CONSTANTS.MONITORING.HS.JOB                           = '/monitoring/application/history/jobs/job.json';
CONSTANTS.MONITORING.HS.JOBS                          = '/monitoring/application/history/jobs.json';
CONSTANTS.MONITORING.HS.CONF                          = '/monitoring/application/history/jobs/job/configuration.json';
CONSTANTS.MONITORING.HS.TASKS                         = '/monitoring/application/history/jobs/job/tasks.json';
CONSTANTS.MONITORING.HS.COUNTERS                      = '/monitoring/application/history/jobs/job/counters.json';

// Cluster Nodes
CONSTANTS.MONITORING.CLUSTERNODES.TIMESERIES          = '/monitoring/clusternode/timeseries.json';
CONSTANTS.MONITORING.CLUSTERNODES.NODES               = '/monitoring/clusternode/nodes.json';

// Namenode
CONSTANTS.MONITORING.NAMENODE.JVM_HEAP                = '/monitoring/namenode/jvmheap.json';
CONSTANTS.MONITORING.NAMENODE.DFS_USAGE               = '/monitoring/namenode/dfsusage.json';
CONSTANTS.MONITORING.NAMENODE.BLOCK_STATUS            = '/monitoring/namenode/blockstatus.json';
CONSTANTS.MONITORING.NAMENODE.INFO                    = '/monitoring/namenode/info.json';

// MapR CLDB
CONSTANTS.MONITORING.CLDB.METRICS                     = '/monitoring/cldb/metrics.json';

///////////////////////////////////////
// Admin
///////////////////////////////////////

CONSTANTS.SYSTEM.WEBSOCKET              = '/websocket';

CONSTANTS.SYSTEM.MENU.SELECT             = '/system/menu/select';//'/resources/data/menu.json';//'/admin/menu/select';
CONSTANTS.SYSTEM.MENU.SELECT_NODE        = '/system/menu/selectNode';
CONSTANTS.SYSTEM.MENU.SAVE               = '/system/menu/save';

///////////////////////////////////////
// SERVICELIST
///////////////////////////////////////

CONSTANTS.SERVICELIST.SELECT            = '/resources/data/servicelist.json';

///////////////////////////////////////
// EVENTLIST
///////////////////////////////////////

CONSTANTS.EVENTLIST.SELECT              = '/resources/data/eventlist.json';

///////////////////////////////////////
// USER
///////////////////////////////////////

CONSTANTS.USER.QA.SELECT                = '/resources/data/qalist.json';
CONSTANTS.USER.PREFERENCE = {};
CONSTANTS.USER.PREFERENCE.EVENT = {};
CONSTANTS.USER.PREFERENCE.EVENT.LIST = "/user/preference/event/list.json";

///////////////////////////////////////
// Setting > Metadata
///////////////////////////////////////

CONSTANTS.SETTING.META.TREE             = '/setting/metadata/tree';
CONSTANTS.SETTING.META.GRID             = '/setting/metadata/grid';
CONSTANTS.SETTING.META.SAVE             = '/setting/metadata/save';

///////////////////////////////////////
// Constants
///////////////////////////////////////

CONSTANTS.GRID_SIZE_PER_PAGE        = 22;
CONSTANTS.ROOT                      = '/';
CONSTANTS.FILE_UPLOAD_TIMEOUT       = 120000;

///////////////////////////////////////
// Workflow Designer
///////////////////////////////////////

CONSTANTS.DESIGNER.SAVE = "/designer/save.json";
CONSTANTS.DESIGNER.LOAD = "/designer/load.json";
CONSTANTS.DESIGNER.RUN = "/designer/run.json";
CONSTANTS.DESIGNER.COPY = "/designer/copy.json";
CONSTANTS.DESIGNER.SHOW = "/designer/show.json";
CONSTANTS.DESIGNER.GET = "/designer/get.json";
CONSTANTS.DESIGNER.NEW = "/designer/new.json";
CONSTANTS.DESIGNER.DELETE = "/designer/delete.json";
CONSTANTS.DESIGNER.RENAME = "/designer/rename.json";
CONSTANTS.DESIGNER.STATUS = "/designer/status.json";
CONSTANTS.DESIGNER.STATUS_DETAIL = "/designer/status/detail.json";

CONSTANTS.DESIGNER.VM = {};
CONSTANTS.DESIGNER.VM.PROPERTIES = "/designer/vm/properties.json";

CONSTANTS.DESIGNER.TREE = {};
CONSTANTS.DESIGNER.TREE.GET = "/tree/get.json";

///////////////////////////////////////
// Batch Job
///////////////////////////////////////

CONSTANTS.BATCH.GET_WORKFLOW          = '/batch/get.json';
CONSTANTS.BATCH.REGIST                = '/batch/regist.json';
CONSTANTS.BATCH.LIST                  = '/batch/list.json';
CONSTANTS.BATCH.SUSPEND               = '/batch/suspend.json';
CONSTANTS.BATCH.RESUME                = '/batch/resume.json';
CONSTANTS.BATCH.STOP                  = '/batch/stop.json';
CONSTANTS.BATCH.WORKFLOW_ENGINE       = '/batch/summary.json';
CONSTANTS.BATCH.METRICS               = '/batch/metrics.json';
///////////////////////////////////////
// Admin > Workflow Engine
///////////////////////////////////////

CONSTANTS.ADMIN.WE.REGIST_JOB           = '/admin/engine/regist.json';
CONSTANTS.ADMIN.WE.ADD_ENGINE           = '/admin/engine/add.json';
CONSTANTS.ADMIN.WE.DEL_ENGINE           = '/admin/engine/delete.json';
CONSTANTS.ADMIN.WE.LIST_ENGINES         = '/resources/data/engine.json';//'/admin/engine/engines.json';
CONSTANTS.ADMIN.WE.GET_ENVS             = '/admin/engine/envs.json';
CONSTANTS.ADMIN.WE.GET_PROPS            = '/admin/engine/props.json';
CONSTANTS.ADMIN.WE.GET_TRIGGERS         = '/admin/engine/triggers.json';
CONSTANTS.ADMIN.WE.GET_RUNNING_JOBS     = '/admin/engine/running.json';
CONSTANTS.ADMIN.WE.GET_AIO              = '/admin/engine/aio.json';

///////////////////////////////////////
// Admin > Hadoop
///////////////////////////////////////

CONSTANTS.ADMIN.HADOOP.GET_HADOOP_CLUSTERS      = '/admin/hadoop/clusters.json';
CONSTANTS.ADMIN.HADOOP.ADD_HADOOP_CLUSTER       = '/admin/hadoop/add.json';
CONSTANTS.ADMIN.HADOOP.UPDATE_HADOOP_CLUSTER    = '/admin/hadoop/update.json';
CONSTANTS.ADMIN.HADOOP.DELETE_HADOOP_CLUSTER    = '/admin/hadoop/delete.json';
CONSTANTS.ADMIN.HADOOP.UPDATE_HADOOP_CONF       = '/admin/hadoop/jobConf.json';

///////////////////////////////////////
// Admin > Hive
///////////////////////////////////////

CONSTANTS.ADMIN.HIVE.GET_HIVE_SERVERS     = '/admin/hive/servers.json';
CONSTANTS.ADMIN.HIVE.ADD_HIVE_SERVER      = '/admin/hive/add.json';
CONSTANTS.ADMIN.HIVE.UPDATE_HIVE_SERVER   = '/admin/hive/update.json';
CONSTANTS.ADMIN.HIVE.DELETE_HIVE_SERVER   = '/admin/hive/delete.json';

///////////////////////////////////////
// Admin > User
///////////////////////////////////////

CONSTANTS.ADMIN.USER.GET_GROUP          = '/admin/user/group/get.json';
CONSTANTS.ADMIN.USER.ADD_GROUP          = '/admin/user/group/add.json';
CONSTANTS.ADMIN.USER.DELETE_GROUP       = '/admin/user/group/delete.json';
CONSTANTS.ADMIN.USER.GET_USER_ALL       = '/admin/user/list.json';
CONSTANTS.ADMIN.USER.GET_USER           = '/admin/user/userGroup.json';
CONSTANTS.ADMIN.USER.ADD_USER           = '/admin/user/add.json';
CONSTANTS.ADMIN.USER.DELETE_USER        = '/admin/user/delete.json';
CONSTANTS.ADMIN.USER.UPDATE_USER        = '/admin/user/update.json';
CONSTANTS.ADMIN.USER.GET_AUTHORITY      = '/admin/user/authority.json';
CONSTANTS.ADMIN.USER.GET_GRADE_All      = '/admin/user/grade/list.json';

///////////////////////////////////////
// Admin > HDFS
///////////////////////////////////////

CONSTANTS.ADMIN.HDFS.GET_HDFS_AUTH          = '/admin/hdfs/hdfsAuthAll.json';
CONSTANTS.ADMIN.HDFS.ADD_HDFS_AUTH          = '/admin/hdfs/add.json';
CONSTANTS.ADMIN.HDFS.DELETE_HDFS_AUTH       = '/admin/hdfs/delete.json';
CONSTANTS.ADMIN.HDFS.UPDATE_HDFS_PATH       = '/admin/hdfs/updatePath.json';
CONSTANTS.ADMIN.HDFS.UPDATE_HDFS_AUTH       = '/admin/hdfs/update.json';
CONSTANTS.ADMIN.HDFS.GET_HDFS_AUTH_DETAIL   = '/admin/hdfs/hdfsAuthDetail.json';
CONSTANTS.ADMIN.HDFS.SAVE_HDFS_AUTH         = '/admin/hdfs/save.json';
CONSTANTS.ADMIN.HDFS.UPDATE_PATTERN_INFO    = '/admin/hdfs/updatePatternInfo.json';

///////////////////////////////////////
// Admin > Menu
///////////////////////////////////////

CONSTANTS.ADMIN.MENU.SELECT_TREE_MENU      = '/admin/menu/selectMenuList.json';
CONSTANTS.ADMIN.MENU.UPDATE_TREE_MENU      = '/admin/menu/updateMenu.json';
CONSTANTS.ADMIN.MENU.INSERT_TREE_MENU      = '/admin/menu/insertMenu.json';
CONSTANTS.ADMIN.MENU.DELETE_TREE_MENU      = '/admin/menu/deleteMenu.json';

///////////////////////////////////////
// Admin > Authority
///////////////////////////////////////

CONSTANTS.ADMIN.AUTH.SELECT_GRADE          = '/admin/auth/selectGrade.json';
CONSTANTS.ADMIN.AUTH.SELECT_MENU_AUTH      = '/admin/auth/selectMenuAuth.json';
CONSTANTS.ADMIN.AUTH.INSERT_MENU_AUTH      = '/admin/auth/insertMenuAuth.json';
CONSTANTS.ADMIN.AUTH.COUNT_AUTH            = '/admin/auth/countAuth.json';
CONSTANTS.ADMIN.AUTH.SAVE_AUTH             = '/admin/auth/saveAuth.json';

///////////////////////////////////////
// Pig > Editor
///////////////////////////////////////

CONSTANTS.PIG.SAVE      = '/pig/save.json';
CONSTANTS.PIG.LOAD      = '/pig/load.json';
CONSTANTS.PIG.RUN       = '/pig/run.json';
CONSTANTS.PIG.LIST      = '/pig/list.json';
CONSTANTS.PIG.GET_LOG   = '/pig/log.json';
CONSTANTS.PIG.DELETE    = '/pig/delete.json';
CONSTANTS.PIG.EXECUTE   = '/pig/execute.json';

///////////////////////////////////////
// Hive > Editor
///////////////////////////////////////

CONSTANTS.HIVE.HISTORY          = '/hive/query/history.json';
CONSTANTS.HIVE.EXECUTE          = '/hive/query/execute.json';
CONSTANTS.HIVE.CANCEL           = '/hive/query/cancel.json';
CONSTANTS.HIVE.RESULTS          = '/hive/query/results.json';
CONSTANTS.HIVE.EXPLAIN          = '/hive/query/explain.json';
CONSTANTS.HIVE.LIST             = '/hive/query/list.json';
CONSTANTS.HIVE.SAVE             = '/hive/query/save.json';
CONSTANTS.HIVE.DELETE           = '/hive/query/delete.json';
CONSTANTS.HIVE.DB               = '/hive/query/databases.json';
CONSTANTS.HIVE.CHECK_SIZE       = '/hive/query/size.json';
CONSTANTS.HIVE.DOWNLOAD         = '/hive/query/download.json';
CONSTANTS.HIVE.GET_LOG          = '/hive/query/getLog.json';
CONSTANTS.HIVE.GET_LOG_ASYNC    = '/hive/query/getLogAsync.json';
CONSTANTS.HIVE.GET_PAGE         = '/hive/query/getPage.json';


///////////////////////////////////////
// Hive > Browser
///////////////////////////////////////

CONSTANTS.HIVE.METASTORE.GET_DATABASES            = '/hive/metastore/databases.json';
CONSTANTS.HIVE.METASTORE.GET_TABLES               = '/hive/metastore/tables.json';
CONSTANTS.HIVE.METASTORE.GET_COLUMNS              = '/hive/metastore/columns.json';
CONSTANTS.HIVE.METASTORE.GET_PARTITIONS           = '/hive/metastore/partitions.json';
CONSTANTS.HIVE.METASTORE.CREATE_DATABASE          = '/hive/metastore/createDB.json';
CONSTANTS.HIVE.METASTORE.DROP_DATABASE            = '/hive/metastore/dropDB.json';
CONSTANTS.HIVE.METASTORE.CREATE_TABLE             = '/hive/metastore/createTable.json';
CONSTANTS.HIVE.METASTORE.DROP_TABLE               = '/hive/metastore/dropTable.json';
CONSTANTS.HIVE.METASTORE.TABLE_INFO               = '/hive/metastore/tableInfo.json';
CONSTANTS.HIVE.METASTORE.ALTER_TABLE              = '/hive/metastore/alterTable.json';

///////////////////////////////////////
// File System > AUDIT
///////////////////////////////////////

CONSTANTS.FS.AUDIT.LIST                 = '/fs/audit/list.json';
CONSTANTS.FS.AUDIT.TOP10                = '/fs/audit/top10.json';
CONSTANTS.FS.AUDIT.STATUS               = '/fs/audit/nowStatus.json';
CONSTANTS.FS.AUDIT.TREND                = '/fs/audit/trend.json';
CONSTANTS.FS.AUDIT.INSERT               = '/fs/audit/insert.json';

///////////////////////////////////////
// File System > HDFS
///////////////////////////////////////

// NEW
CONSTANTS.FS.HDFS_GET_DIRECTORY                 = '/fs/hdfs/directory.json';
CONSTANTS.FS.HDFS_GET_FILE                      = '/fs/hdfs/file.json';
CONSTANTS.FS.HDFS_CREATE_DIRECTORY              = '/fs/hdfs/createDirectory.json';
CONSTANTS.FS.HDFS_DELETE_DIRECTORY              = '/fs/hdfs/deleteDirectory.json';
CONSTANTS.FS.HDFS_RENAME_DIRECTORY              = '/fs/hdfs/renameDirectory.json';
CONSTANTS.FS.HDFS_MOVE_DIRECTORY                = '/fs/hdfs/moveDirectory.json';
CONSTANTS.FS.HDFS_COPY_DIRECTORY                = '/fs/hdfs/copyDirectory.json';
CONSTANTS.FS.HDFS_GET_DIRECTORY_INFO            = '/fs/hdfs/getDirectoryInfo.json';
CONSTANTS.FS.HDFS_UPLOAD_FILE                   = '/fs/hdfs/upload.json';
CONSTANTS.FS.HDFS_DOWNLOAD_FILE                 = '/fs/hdfs/download.json';
CONSTANTS.FS.HDFS_GET_FILE_INFO                 = '/fs/hdfs/getFileInfo.json';
CONSTANTS.FS.HDFS_COPY_FILE                     = '/fs/hdfs/copyFiles.json';
CONSTANTS.FS.HDFS_MOVE_FILE                     = '/fs/hdfs/moveFiles.json';
CONSTANTS.FS.HDFS_RENAME_FILE                   = '/fs/hdfs/renameFile.json';
CONSTANTS.FS.HDFS_DELETE_FILE                   = '/fs/hdfs/deleteFiles.json';
CONSTANTS.FS.HDFS_GET_DEFAULT_FILE_CONTENTS     = '/fs/hdfs/initViewFileContents.json';
CONSTANTS.FS.HDFS_GET_FILE_CONTENTS             = '/fs/hdfs/viewFileContents.json';
CONSTANTS.FS.HDFS_GET_MERGE_FILE                = '/fs/hdfs/mergeFiles';
CONSTANTS.FS.HDFS_SET_PERMISSION                = '/fs/hdfs/setPermission';

///////////////////////////////////////
// Workflow Management > Dashboard
///////////////////////////////////////

CONSTANTS.DASHBOARD.GET_YARN_ID             = '/dashboard/yarnId.json';
CONSTANTS.DASHBOARD.GET_MR_ID               = '/dashboard/mrId.json';
CONSTANTS.DASHBOARD.GET_ACTION_HISTORY      = '/dashboard/actions.json';
CONSTANTS.DASHBOARD.GET_WORKFLOW_HISTORY    = '/dashboard/workflows.json';
CONSTANTS.DASHBOARD.GET_WORKFLOW_XML        = '/dashboard/xml.json';
CONSTANTS.DASHBOARD.GET_LOG                 = '/dashboard/log.json';
CONSTANTS.DASHBOARD.GET_LOGS                = '/dashboard/logs.json';
CONSTANTS.DASHBOARD.GET_SCRIPT              = '/dashboard/script.json';
CONSTANTS.DASHBOARD.GET_WORKFLOW            = '/dashboard/workflow.json';
CONSTANTS.DASHBOARD.KILL                    = '/dashboard/kill.json';
CONSTANTS.DASHBOARD.JOBS                    = '/dashboard/allJobs.json';
CONSTANTS.DASHBOARD.HADOOP_JOB_CONF         = '/dashboard/jobConf.json';
CONSTANTS.DASHBOARD.HADOOP_JOB_COUNTERS     = '/dashboard/jobCounters.json';
CONSTANTS.DASHBOARD.HADOOP_MR_SUMMARY       = '/dashboard/mapreduceSummary.json';
CONSTANTS.DASHBOARD.HADOOP_JOB              = '/dashboard/job.json';
CONSTANTS.DASHBOARD.HADOOP_PROGRESS_MAP     = '/dashboard/map.json';
CONSTANTS.DASHBOARD.HADOOP_PROGRESS_REDUCE  = '/dashboard/reduce.json';
CONSTANTS.DASHBOARD.HADOOP_JOB_TRACKER      = '/dashboard/jobTracker.json';
CONSTANTS.DASHBOARD.HADOOP_HDFS             = '/dashboard/hdfs.json';
CONSTANTS.DASHBOARD.HADOOP_CLUSTER_SUMMARY  = '/dashboard/clusterSummary.json';
CONSTANTS.DASHBOARD.HADOOP_TASK_TRACKERS    = '/dashboard/taskTrackers.json';
CONSTANTS.DASHBOARD.JOB_CONF_DOWNLOAD_FILE  = '/dashboard/download.json';
CONSTANTS.DASHBOARD.GET_FLAMINGO_JOB        = '/dashboard/flamingoJob.json';
CONSTANTS.DASHBOARD.JT_TASK_TRACKERS        = '/dashboard/jt/taskTrackers.json';
CONSTANTS.DASHBOARD.JT_TASK_TRACKER_DETAIL  = '/dashboard/jt/taskTrackerDetail.json';
CONSTANTS.DASHBOARD.DATANODES               = '/dashboard/datanodes.json';
CONSTANTS.DASHBOARD.JT_FAILED_TASKS         = '/dashboard/jt/failedTasks.json';
CONSTANTS.DASHBOARD.TASK.GET                = "/dashboard/task/get.json";
CONSTANTS.DASHBOARD.TASK.LIST               = "/dashboard/task/list.json";
CONSTANTS.DASHBOARD.TASK.LOG                = "/dashboard/task/log.json";
CONSTANTS.DASHBOARD.WORKFLOW_SUMMARY        = "/dashboard/timeseries.json";

///////////////////////////////////////
// Workflow Management > Designer
///////////////////////////////////////

CONSTANTS.REST_RENAME_TREE            = '/rest/tree/rename.do.json';
CONSTANTS.REST_GET_WORKFLOW           = '/rest/job/workflow.do.json';
CONSTANTS.REST_MOVE_TREE              = '/rest/designer/move.do.json';
CONSTANTS.REST_HDFS_GET_FILE          = '/rest/hdfs/file.do.json';
CONSTANTS.REST_HDFS_FS_STATUS         = '/rest/hadoop/fileSystemStatus.do.json';

///////////////////////////////////////
// Hadoop 2.x Monitoring
///////////////////////////////////////

CONSTANTS.MONITORING.GET_RESOURCEMANAGER_INFO         = '/monitoring/resourcemanager/info.json';
CONSTANTS.MONITORING.GET_NAMENODE                     = '/monitoring/getNamenode.json';
CONSTANTS.MONITORING.GET_LIVENODES                    = '/monitoring/namenode/nodes/live.json';
CONSTANTS.MONITORING.GET_DEADNODES                    = '/monitoring/namenode/nodes/dead.json';
CONSTANTS.MONITORING.GET_DECOMMISSIONINGNODES         = '/monitoring/namenode/nodes/decommission.json';
CONSTANTS.MONITORING.GET_RESOURCEMANAGER              = '/monitoring/getResourcemanager.json';
CONSTANTS.MONITORING.GET_APPLICATIONS                 = '/monitoring/getApplications.json';
CONSTANTS.MONITORING.GET_MRJOBS                       = '/monitoring/getMRjobs.json';
CONSTANTS.MONITORING.GET_JOB                          = '/monitoring/getJob.json';
CONSTANTS.MONITORING.GET_TASKS                        = '/monitoring/getTasks.json';
CONSTANTS.MONITORING.GET_TASKCOUNTERS                 = '/monitoring/getTaskCounters.json';
CONSTANTS.MONITORING.GET_JOBCONF                      = '/monitoring/getJobConf.json';
CONSTANTS.MONITORING.GET_COUNTERS                     = '/monitoring/getCounters.json';
CONSTANTS.MONITORING.GET_HDFS_TOP5                    = '/monitoring/hdfs/top5.json';

// Cluster Nodes
CONSTANTS.MONITORING.CLUSTERNODES.TIMESERIES          = '/monitoring/clusternode/timeseries.json';
CONSTANTS.MONITORING.CLUSTERNODES.NODES               = '/monitoring/clusternode/nodes.json';

///////////////////////////////////////
// Pivotal HAWQ
///////////////////////////////////////

CONSTANTS.HAWQ.BROWSER.CONNECT                 = '/hawq/browser/connect';
CONSTANTS.HAWQ.BROWSER.DATABASES               = '/hawq/browser/databases';
CONSTANTS.HAWQ.BROWSER.SCHEMAS                 = '/hawq/browser/schemas';
CONSTANTS.HAWQ.BROWSER.TABLES                  = '/hawq/browser/tables';
CONSTANTS.HAWQ.BROWSER.VIEWS                   = '/hawq/browser/views';
CONSTANTS.HAWQ.BROWSER.FUNCTIONS               = '/hawq/browser/functions';
CONSTANTS.HAWQ.BROWSER.EXTERNAL_TABLES         = '/hawq/browser/externalTables';
CONSTANTS.HAWQ.BROWSER.COLUMNS                 = '/hawq/browser/columns';
CONSTANTS.HAWQ.BROWSER.OBJECT_METADATAS        = '/hawq/browser/objectMetadatas';
CONSTANTS.HAWQ.BROWSER.OBJECT_DEF              = '/hawq/browser/objectDef';
CONSTANTS.HAWQ.BROWSER.PARTITION_DETAIL        = '/hawq/browser/partitionDetail';
CONSTANTS.HAWQ.BROWSER.PARTITIONS_TREE         = '/hawq/browser/partitionsTree';
CONSTANTS.HAWQ.BROWSER.DROP_DATABASE           = '/hawq/browser/dropDatabase';
CONSTANTS.HAWQ.BROWSER.DROP_SCHEMA             = '/hawq/browser/dropSchema';
CONSTANTS.HAWQ.BROWSER.TABLESPACES             = '/hawq/browser/tablespaces';
CONSTANTS.HAWQ.BROWSER.USERS                   = '/hawq/browser/users';
CONSTANTS.HAWQ.BROWSER.CREATE_DATABASE         = '/hawq/browser/createDatabase';
CONSTANTS.HAWQ.BROWSER.CREATE_SCHEMA           = '/hawq/browser/createSchema';
CONSTANTS.HAWQ.BROWSER.DROP_TABLE              = '/hawq/browser/dropTable';
CONSTANTS.HAWQ.BROWSER.DATA_TYPE               = '/hawq/browser/dataType';
CONSTANTS.HAWQ.BROWSER.CREATE_TABLE            = '/hawq/browser/createTable';
CONSTANTS.HAWQ.BROWSER.DROP_EXTERNAL_TABLE     = '/hawq/browser/dropExternalTable';
CONSTANTS.HAWQ.BROWSER.CUSTOM_FORMATTER        = '/hawq/browser/customFormatter';
CONSTANTS.HAWQ.BROWSER.CREATE_EXTERNAL_TABLE   = '/hawq/browser/createExternalTable';
CONSTANTS.HAWQ.BROWSER.DROP_VIEW               = '/hawq/browser/dropView';
CONSTANTS.HAWQ.BROWSER.CHANGE_DATABASE         = '/hawq/browser/changeDatabase';
CONSTANTS.HAWQ.BROWSER.DROP_FUNCTION           = '/hawq/browser/dropFunction';
CONSTANTS.HAWQ.BROWSER.TABLE_DETAIL            = '/hawq/browser/tableDetail';
CONSTANTS.HAWQ.BROWSER.ALTER_TABLE             = '/hawq/browser/alterTable';
CONSTANTS.HAWQ.BROWSER.ALTER_COLUMN            = '/hawq/browser/alterColumn';
CONSTANTS.HAWQ.BROWSER.CONSTRAINTS             = '/hawq/browser/constraints';
CONSTANTS.HAWQ.BROWSER.ALTER_CONSTRAINT        = '/hawq/browser/alterConstraint';

CONSTANTS.HAWQ.EDITOR.EXECUTE                  = '/hawq/editor/execute';
CONSTANTS.HAWQ.EDITOR.VIEW_PLAN                = '/hawq/editor/viewPlan';
CONSTANTS.HAWQ.EDITOR.KILL_SESSION             = '/hawq/editor/killSession';
CONSTANTS.HAWQ.EDITOR.DOWNLOAD_RESULT          = '/hawq/editor/downloadResult';

CONSTANTS.HAWQ.AUTH.RESOURCE_QUEUES            = '/hawq/auth/resourceQueues';
CONSTANTS.HAWQ.AUTH.RESOURCE_QUEUE             = '/hawq/auth/resourceQueue';
CONSTANTS.HAWQ.AUTH.CREATE_RESOURCE_QUEUE      = '/hawq/auth/createResourceQueue';
CONSTANTS.HAWQ.AUTH.DROP_RESOURCE_QUEUE        = '/hawq/auth/dropResourceQueue';
CONSTANTS.HAWQ.AUTH.GROUP_ROLES                = '/hawq/auth/groupRoles';
CONSTANTS.HAWQ.AUTH.LOGIN_ROLES                = '/hawq/auth/loginRoles';
CONSTANTS.HAWQ.AUTH.ROLE                       = '/hawq/auth/role';
CONSTANTS.HAWQ.AUTH.CREATE_ROLE                = '/hawq/auth/createRole';
CONSTANTS.HAWQ.AUTH.ALTER_ROLE                 = '/hawq/auth/alterRole';
CONSTANTS.HAWQ.AUTH.DROP_ROLE                  = '/hawq/auth/dropRole';
CONSTANTS.HAWQ.AUTH.SESSIONS                   = '/hawq/auth/sessions';
CONSTANTS.HAWQ.AUTH.LOCK_TABLES                = '/hawq/auth/lockTables';


///////////////////////////////////////
// Visual
///////////////////////////////////////

CONSTANTS.VISUAL.RELOAD_DATA              = '/visual/reloadData.json';
CONSTANTS.VISUAL.LOAD_HDFS                = '/visual/loadHdfs.json';
CONSTANTS.VISUAL.LOCAL_UPLOAD             = '/visual/upload.json';
CONSTANTS.VISUAL.LIST_VARIABLES_HDFS      = '/visual/listVariablesHdfs.json';
CONSTANTS.VISUAL.LIST_VARIABLES_LOCAL     = '/visual/listVariablesLocal.json';
CONSTANTS.VISUAL.CREATE_PNG               = '/visual/createPng.json';

///////////////////////////////////////
// Preferences
///////////////////////////////////////

CONSTANTS.PREFERENCES.CHANGE_PASSWORD   = '/auth/password.json';


