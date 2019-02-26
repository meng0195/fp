-- 温度数据表
ALTER TABLE `testdata`
ADD COLUMN `ReportFlag`  tinyint NULL DEFAULT 0 COMMENT '标记为报表数据' AFTER `CcT`,
ADD COLUMN `CurveFlag`  tinyint NULL DEFAULT 0 COMMENT '标记为曲线数据' AFTER `ReportFlag`;

-- 测虫数据表
ALTER TABLE `testpest`
ADD COLUMN `CurveFlag`  tinyint NULL DEFAULT 0 COMMENT '标记为曲线数据' AFTER `Videos`;

-- 测气数据表
ALTER TABLE `testgas`
ADD COLUMN `CurveFlag`  tinyint NULL DEFAULT 0 COMMENT '标记为曲线数据' AFTER `TestTag`;

-- 给电缆分区
ALTER TABLE `pointinfo`
ADD COLUMN `Area`  tinyint NULL COMMENT '给电缆分区' AFTER `zAxis`;

-- 记录每个区的温度集合
ALTER TABLE `testdata`
ADD COLUMN `AreaTs`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '每个区的最高温最低温平均温' AFTER `CurveFlag`;

-- 测温模块 2017-11-30
-- yucy
-- v1.0.1
-- 是否是有效点
ALTER TABLE `pointinfo`
ADD COLUMN `Valid`  tinyint NULL COMMENT '是否是有效点' AFTER `Area`;

-- 系统菜单 2017-12-04
-- wc
-- v1.0.2
-- 添加数据管理相关功能菜单
INSERT INTO `sys_menu` VALUES ('29862c21-3660-4b0a-8d38-558532727478', '6d902f19-cb01-4993-8941-2723e7a83ea5', '012003', '备份历史', '备份历史', '/dbm/histery/list', 2, 1, 1, 1);
INSERT INTO `sys_menu` VALUES ('6d902f19-cb01-4993-8941-2723e7a83ea5', '', '012', '数据管理', '数据管理', '', 1, 16, 1, 1);
INSERT INTO `sys_menu` VALUES ('f6eb452d-d02f-4578-b3e2-82b9bf2f5748', '6d902f19-cb01-4993-8941-2723e7a83ea5', '012001', '备份与还原', '备份与还原', '/dbm/to/do', 2, 1, 1, 1);

-- 系统菜单 2017-12-04
-- yucy
-- v1.0.2
-- 添加设备操作记录
INSERT INTO `sys_menu` VALUES ('beddf021-c8cb-4cd6-8d0f-e5dfc60f2f06', '322fd8d4-2935-407d-a416-c03c660fc04b', '004018', '操作记录', '操作记录', '/squery/equip/history', 2, 17, 1, 1);

-- 温度电缆配置 2017-12-26
-- wc
-- v1.0.0
-- 添加设备操作记录
ALTER TABLE `tempinfo`
ADD COLUMN `LeveStr` varchar(1000) COMMENT '层归属标识集合' AFTER `MaxZ`;
ALTER TABLE `pointinfo`
ADD COLUMN `Leve` tinyint NULL DEFAULT 0 COMMENT '层标识' AFTER `Valid`;

-- 计划配置 2017-12-28
-- wc
-- v1.0.0
-- 添加字段-报表数据标识
ALTER TABLE `plantask`
ADD COLUMN `ReportTag` tinyint NULL DEFAULT 0 COMMENT '报表数据标识' AFTER `UserCode`;

-- 计划配置 2018-01-28
-- wc
-- v1.0.0
-- 添加字段-报表数据标识
ALTER TABLE testdata ADD COLUMN MbTag tinyint NULL DEFAULT 0 COMMENT '是否密闭' AFTER AreaTs;
ALTER TABLE testdata ADD COLUMN XzTag tinyint NULL DEFAULT 0 COMMENT '是否熏蒸' AFTER MbTag;
ALTER TABLE testdata ADD COLUMN JccTag tinyint NULL DEFAULT 0 COMMENT '进出仓状态' AFTER XzTag;
ALTER TABLE testdata ADD COLUMN WeaTag tinyint NULL DEFAULT 0 COMMENT '天气' AFTER JccTag;
ALTER TABLE testdatacopy ADD COLUMN MbTag tinyint NULL DEFAULT 0 COMMENT '是否密闭' AFTER AreaTs;
ALTER TABLE testdatacopy ADD COLUMN XzTag tinyint NULL DEFAULT 0 COMMENT '是否熏蒸' AFTER MbTag;
ALTER TABLE testdatacopy ADD COLUMN JccTag tinyint NULL DEFAULT 0 COMMENT '进出仓状态' AFTER XzTag;
ALTER TABLE testdatacopy ADD COLUMN WeaTag tinyint NULL DEFAULT 0 COMMENT '天气' AFTER JccTag;
ALTER TABLE storehouse ADD COLUMN MbTag tinyint NULL DEFAULT 0 COMMENT '是否密闭' AFTER AreaTs;
ALTER TABLE storehouse ADD COLUMN XzTag tinyint NULL DEFAULT 0 COMMENT '是否熏蒸' AFTER MbTag;
ALTER TABLE storehouse ADD COLUMN JccTag tinyint NULL DEFAULT 0 COMMENT '进出仓状态' AFTER XzTag;

-- 模式管理 2018-02-24
-- yucy
-- v1.0.0
-- 添加字段-ARM开放模式信息
ALTER TABLE `equipips`
ADD COLUMN `ArmDioIp`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'ARM开放模式IP' AFTER `Password`,
ADD COLUMN `ArmDioPort`  int NULL COMMENT 'ARM开放模式端口' AFTER `ArmDioIp`;

-- 新建设备能耗记录表 2018-02-27
-- yucy
-- v1.0.0
CREATE TABLE `PowerNotes` (
`ID`  varchar(36) NULL COMMENT '主键' ,
`EquipNo`  int(11) NULL COMMENT '设备编号' ,
`EquipName`  varchar(15) NULL COMMENT '设备名称' ,
`EquipType`  tinyint NULL COMMENT '设备类型' ,
`PowerType`  tinyint NULL COMMENT '能耗类型' ,
`HouseNo`  varchar(3) NULL COMMENT '仓房名称' ,
`StartTime`  datetime NULL COMMENT '启动时间' ,
`EndTime`  datetime NULL COMMENT '结束时间' ,
`StartPower`  double(8,2) NULL COMMENT '启动能耗' ,
`EndPower`  double(8,2) NULL COMMENT '结束能耗' ,
`RunTime`  bigint NULL COMMENT '运行时间' ,
`Power`  double(8,2) NULL COMMENT '消耗能耗' ,
`Status`  tinyint NULL COMMENT '运行状态'
)
;

-- 新增一对ARM窗户DIDO

ALTER TABLE `equipment`
ADD COLUMN `DoWay1`  int NULL COMMENT 'DO位' AFTER `DoWay`,
ADD COLUMN `DiWay3`  int NULL COMMENT 'DI位' AFTER `DiWay2`;

-- 新增绑定风窗DIDO
ALTER TABLE `equipment`
ADD COLUMN `WindDo1`  int NULL COMMENT '绑定风窗Do' AFTER `BindRegister`,
ADD COLUMN `WindDo2`  int NULL COMMENT '绑定风窗Do' AFTER `WindDo1`,
ADD COLUMN `WIndDi1`  int NULL COMMENT '绑定风窗状态位' AFTER `WindDo2`,
ADD COLUMN `WindDi2`  int NULL COMMENT '绑定风窗状态位' AFTER `WIndDi1`;

-- 新增关联设备，针对内环流
ALTER TABLE `equipment`
ADD COLUMN `CAEquipNo`  int NULL COMMENT '关联设备' AFTER `WindDi2`;

-- 新增设备记录表，所有扫到的设备操作信息都放入该表
CREATE TABLE `CtrsNotes` (
`ID`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '主键' ,
`EquipNo`  int(11) NULL DEFAULT NULL COMMENT '设备编号' ,
`EquipName`  varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名称' ,
`EquipType`  tinyint(4) NULL DEFAULT NULL COMMENT '设备类型' ,
`EquipPower`  double(4,0) NULL DEFAULT NULL ,
`HouseNo`  varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仓房名称' ,
`StartTime`  datetime NULL DEFAULT NULL COMMENT '启动时间' ,
`EndTime`  datetime NULL DEFAULT NULL COMMENT '结束时间' ,
`StartPower`  double(8,2) NULL DEFAULT NULL COMMENT '启动能耗' ,
`EndPower`  double(8,2) NULL DEFAULT NULL COMMENT '结束能耗' ,
`RunTime`  bigint(20) NULL DEFAULT NULL COMMENT '运行时间' ,
`Power`  double(8,2) NULL DEFAULT NULL COMMENT '消耗能耗' ,
`Status`  tinyint(4) NULL DEFAULT NULL COMMENT '运行状态' ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=COMPACT
;


-- 新设计能耗配置表
DROP TABLE IF EXISTS `powerinfo`;
CREATE TABLE `powerinfo` (
  `ID` varchar(36) NOT NULL DEFAULT '',
  `HouseNo` varchar(3) DEFAULT NULL,
  `BoardType` tinyint(4) DEFAULT NULL COMMENT '板子类型',
  `PowerIp` varchar(16) DEFAULT NULL COMMENT '能耗板子IP',
  `PowerPort` int(11) DEFAULT NULL COMMENT '端口',
  `RegAdd` tinyint(4) DEFAULT NULL COMMENT '全设备寄存器位',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 修改能耗表为内环流记录表
rename table PowerNotes to NhlNotes;

-- 新建能耗表
DROP TABLE IF EXISTS `powernotes`;
CREATE TABLE `powernotes` (
  `ID` varchar(36) NOT NULL DEFAULT '' COMMENT '主键',
  `HouseNo` varchar(3) DEFAULT NULL COMMENT '仓房编号',
  `CheckTime` datetime DEFAULT NULL COMMENT '检测时间',
  `CheckPower` double(8,2) DEFAULT NULL COMMENT '检测能耗',
  `Power` double(8,2) DEFAULT NULL COMMENT '消耗能耗',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 新增能耗类型
ALTER TABLE `equipment`
ADD COLUMN `PowerType`  tinyint(4) NULL COMMENT '能耗类型' AFTER `Type`;

-- 设备记录表里添加设备类型
ALTER TABLE `ctrsnotes`
ADD COLUMN `PowerType`  tinyint NULL COMMENT '能耗类型' AFTER `EquipType`;

-- 在设备配置里添加模块板类型
ALTER TABLE `equipips`
ADD COLUMN `BoardType`  tinyint NULL COMMENT '模块类型' AFTER `HouseNo`;
