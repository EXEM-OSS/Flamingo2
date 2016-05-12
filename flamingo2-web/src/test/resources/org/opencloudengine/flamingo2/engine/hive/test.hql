CREATE EXTERNAL TABLE test (
	col1 TINYINT,
	col2 SMALLINT,
	col3 INT,
	col4 BIGINT,
	col5 FLOAT,
	col6 DOUBLE,
	col7 DECIMAL,
	col8 TIMESTAMP,
	col9 DATE,
	col10 STRING,
	col11 VARCHAR(10),
	col12 CHAR(10),
	col13 BOOLEAN,
	col14 BINARY,
	col15 ARRAY<STRING>,
	col16 MAP<STRING, STRING>,
	col17 STRUCT<firstname:STRING, nickname:STRING, lastname:STRING>
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
STORED AS TEXTFILE
LOCATION "/samples/omniture/user";

CREATE EXTERNAL TABLE user
(
    swid string,
    birth_dt string,
    gender_cd string
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
STORED AS TEXTFILE
LOCATION "/samples/omniture/user";

CREATE EXTERNAL TABLE product
(
    url string,
    category string
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
STORED AS TEXTFILE
LOCATION "/samples/omniture/product"

CREATE EXTERNAL TABLE omniturelogs
(
col_1 string, col_2 string, col_3 string, col_4 string, col_5 string, col_6 string, col_7 string, col_8 string, col_9 string, col_10 string, col_11 string, col_12 string, col_13 string, col_14 string, col_15 string, col_16 string, col_17 string, col_18 string, col_19 string, col_20 string, col_21 string, col_22 string, col_23 string, col_24 string, col_25 string, col_26 string, col_27 string, col_28 string, col_29 string, col_30 string, col_31 string, col_32 string, col_33 string, col_34 string, col_35 string, col_36 string, col_37 string, col_38 string, col_39 string, col_40 string, col_41 string, col_42 string, col_43 string, col_44 string, col_45 string, col_46 string, col_47 string, col_48 string, col_49 string, col_50 string, col_51 string, col_52 string, col_53 string, col_54 string, col_55 string, col_56 string, col_57 string, col_58 string, col_59 string, col_60 string, col_61 string, col_62 string, col_63 string, col_64 string, col_65 string, col_66 string, col_67 string, col_68 string, col_69 string, col_70 string, col_71 string, col_72 string, col_73 string, col_74 string, col_75 string, col_76 string, col_77 string, col_78 string, col_79 string, col_80 string, col_81 string, col_82 string, col_83 string, col_84 string, col_85 string, col_86 string, col_87 string, col_88 string, col_89 string, col_90 string, col_91 string, col_92 string, col_93 string, col_94 string, col_95 string, col_96 string, col_97 string, col_98 string, col_99 string, col_100 string, col_101 string, col_102 string, col_103 string, col_104 string, col_105 string, col_106 string, col_107 string, col_108 string, col_109 string, col_110 string, col_111 string, col_112 string, col_113 string, col_114 string, col_115 string, col_116 string, col_117 string, col_118 string, col_119 string, col_120 string, col_121 string, col_122 string, col_123 string, col_124 string, col_125 string, col_126 string, col_127 string, col_128 string, col_129 string, col_130 string, col_131 string, col_132 string, col_133 string, col_134 string, col_135 string, col_136 string, col_137 string, col_138 string, col_139 string, col_140 string, col_141 string, col_142 string, col_143 string, col_144 string, col_145 string, col_146 string, col_147 string, col_148 string, col_149 string, col_150 string, col_151 string, col_152 string, col_153 string, col_154 string, col_155 string, col_156 string, col_157 string, col_158 string, col_159 string, col_160 string, col_161 string, col_162 string, col_163 string, col_164 string, col_165 string, col_166 string, col_167 string, col_168 string, col_169 string, col_170 string, col_171 string, col_172 string, col_173 string, col_174 string, col_175 string, col_176 string, col_177 string, col_178 string
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' STORED AS TEXTFILE
LOCATION "/samples/omniture/weblog";

CREATE VIEW omniture AS
SELECT
    col_2 ts,
    col_8 ip,
    col_13 url,
    col_14 swid,
    col_50 city,
    col_51 country,
    col_53 state
from omniturelogs;

