create table BPEL_CORRELATION_PROP (ID bigint not null, NAME varchar(255), NAMESPACE varchar(255), VALUE varchar(255), CORR_SET_ID bigint, INSERT_TIME timestamp, primary key (ID));
create table BPEL_CORRELATION_SET (ID bigint not null, VALUE varchar(255), CORR_SET_NAME varchar(255), SCOPE_ID bigint, PIID bigint, PROCESS_ID bigint, INSERT_TIME timestamp, primary key (ID));
create table BPEL_CORRELATOR (ID bigint not null, CID varchar(255), PROCESS_ID bigint, lock bigint, INSERT_TIME timestamp, primary key (ID));
create table BPEL_CORRELATOR_MESSAGE (ID bigint not null, CORRELATOR bigint, MEX bigint, INSERT_TIME timestamp, primary key (ID));
create table BPEL_CORRELATOR_MESSAGE_CKEY (ID bigint not null, CKEY varchar(255), CORRELATOR_MESSAGE_ID bigint, INSERT_TIME timestamp, primary key (ID));
create table BPEL_CORRELATOR_SELECTOR (ID bigint not null, PIID bigint, CORRELATOR bigint, SELGRPID varchar(255), CKEY varchar(255), IDX integer, INSERT_TIME timestamp, primary key (ID));
create table BPEL_EVENT (ID bigint not null, IID bigint, PID bigint, TSTAMP timestamp, TYPE varchar(255), DETAIL clob(32000), LDATA_ID bigint, SID bigint, INSERT_TIME timestamp, primary key (ID));
create table BPEL_INSTANCE (ID bigint not null, INSTANTIATING_CORRELATOR bigint, FAULT varchar(255), LDATA_ID bigint, PREVIOUS_STATE smallint, PROCESS_ID bigint, STATE smallint, LAST_ACTIVE_DT timestamp, SEQUENCE bigint, INSERT_TIME timestamp, primary key (ID));
create table BPEL_MESSAGE (ID bigint not null, MEX bigint, TYPE varchar(255), DATA bigint, INSERT_TIME timestamp, primary key (ID));
create table BPEL_MESSAGE_EXCHANGE (ID bigint not null, PORT_TYPE varchar(255), CHANNEL_NAME varchar(255), CLIENTKEY varchar(255), LDATA_EPR_ID bigint, REQUEST bigint, RESPONSE bigint, INSERT_DT timestamp, OPERATION varchar(255), STATE varchar(255), PROCESS bigint, INSTANCE bigint, DIR char(1), PLINK_MODELID integer, PATTERN varchar(255), CORR_STATUS varchar(255), FAULT_TYPE varchar(255), CALLEE varchar(255), INSERT_TIME timestamp, primary key (ID));
create table BPEL_MEX_PROPS (MEX bigint not null, VALUE varchar(8000), NAME varchar(255) not null, primary key (MEX, NAME));
create table BPEL_PLINK_VAL (ID bigint not null, PARTNER_LINK varchar(100) not null, PARTNERROLE varchar(100), MYROLE_EPR bigint, PARTNERROLE_EPR bigint, PROCESS bigint, SCOPE bigint, SVCNAME varchar(255), MYROLE varchar(100), MODELID integer, INSERT_TIME timestamp, primary key (ID));
create table BPEL_PROCESS (ID bigint not null, PROCID varchar(255) not null unique, deployer varchar(255), deploydate timestamp, type varchar(255), version integer, RETIRED smallint, ACTIVE smallint, DEPLOYURI varchar(255), CBP bigint, INSERT_TIME timestamp, primary key (ID));
create table BPEL_PROCESS_PROPERTY (ID bigint not null, PROPNAME varchar(255), PROPNS varchar(255), SIMPLE_CNT varchar(255), MIXED_CNT clob(1000000000), PROCESS_ID bigint, INSERT_TIME timestamp, primary key (ID));
create table BPEL_SCOPE (ID bigint not null, PIID bigint, PARENT_SCOPE_ID bigint, STATE varchar(255) not null, NAME varchar(255) not null, MODELID integer, INSERT_TIME timestamp, primary key (ID));
create table BPEL_XML_DATA (ID bigint not null, LDATA_ID bigint, NAME varchar(255) not null, SCOPE_ID bigint, PIID bigint, IS_SIMPLE_TYPE smallint, INSERT_TIME timestamp, primary key (ID));
create table LARGE_DATA (ID bigint not null, BIN_DATA blob(2G), INSERT_TIME timestamp, primary key (ID));
create table PXE_DOMAIN (id varchar(255) not null, primary key (id));
create table PXE_MESSAGE (ID bigint not null, INSERT_TIME timestamp, primary key (ID));
create table PXE_MESSAGE_EXCHANGE (ID varchar(255) not null, PORT_TYPE varchar(255), SYSTEM_ID varchar(255), CHANNEL_NAME varchar(255), LDATA_ID bigint, LDATA_SEPR_ID bigint, LDATA_DEPR_ID bigint, SENDPOINT_SIMPLE_TYPE smallint, DENDPOINT_SIMPLE_TYPE smallint, INPUT_MESSAGE_ID bigint, OUTPUT_MESSAGE_ID bigint, INSERT_DT timestamp, OP_NAME varchar(255), STATE integer, PINNED smallint, primary key (ID));
create table PXE_MESSAGE_PART (ID bigint not null, LDATA_ID bigint, MESSAGE_ID bigint, PARTNAME varchar(255), INSERT_TIME timestamp, primary key (ID));
create table PXE_SYSTEM (UUID varchar(255) not null, SAR_LDATA_ID bigint, DOMAIN_ID varchar(255), NAME varchar(255), SDD_LDATA_ID bigint, DEPLOYED smallint, ACTIVE smallint, primary key (UUID));
create table VAR_PROPERTY (ID bigint not null, XML_DATA_ID bigint, PROP_VALUE varchar(255), PROP_NAME varchar(255) not null, INSERT_TIME timestamp, primary key (ID));
alter table BPEL_CORRELATION_PROP add constraint FK4EC9DDAA17CC4573 foreign key (CORR_SET_ID) references BPEL_CORRELATION_SET;
alter table BPEL_CORRELATION_SET add constraint FKB838191B357A6E10 foreign key (PIID) references BPEL_INSTANCE;
alter table BPEL_CORRELATION_SET add constraint FKB838191BE3239452 foreign key (SCOPE_ID) references BPEL_SCOPE;
alter table BPEL_CORRELATION_SET add constraint FKB838191B48037472 foreign key (PROCESS_ID) references BPEL_PROCESS;
create index IDX_CORRELATOR_CID on BPEL_CORRELATOR (CID);
alter table BPEL_CORRELATOR add constraint FKF50EFA3348037472 foreign key (PROCESS_ID) references BPEL_PROCESS;
create index IDX_CORRELATORMESSAGE_CID on BPEL_CORRELATOR_MESSAGE (CORRELATOR);
alter table BPEL_CORRELATOR_MESSAGE add constraint FK555D49DB57595642 foreign key (MEX) references BPEL_MESSAGE_EXCHANGE;
alter table BPEL_CORRELATOR_MESSAGE add constraint FK555D49DB58F06E3A foreign key (CORRELATOR) references BPEL_CORRELATOR;
create index IDX_BPEL_CORRELATOR_MESSAGE_CKEY on BPEL_CORRELATOR_MESSAGE_CKEY (CKEY);
alter table BPEL_CORRELATOR_MESSAGE_CKEY add constraint FK8997F70088EF56DF foreign key (CORRELATOR_MESSAGE_ID) references BPEL_CORRELATOR_MESSAGE;
create index IDX_SELECTOR_CKEY on BPEL_CORRELATOR_SELECTOR (CKEY);
create index IDX_CORRELATORSELECTOR_CORRELATOR on BPEL_CORRELATOR_SELECTOR (CORRELATOR);
create index IDX_SELECTOR_SELGRPID on BPEL_CORRELATOR_SELECTOR (SELGRPID);
alter table BPEL_CORRELATOR_SELECTOR add constraint FKB8DCE46B357A6E10 foreign key (PIID) references BPEL_INSTANCE;
alter table BPEL_CORRELATOR_SELECTOR add constraint FKB8DCE46B58F06E3A foreign key (CORRELATOR) references BPEL_CORRELATOR;
alter table BPEL_EVENT add constraint FKAA6D6730BE166846 foreign key (LDATA_ID) references LARGE_DATA;
alter table BPEL_EVENT add constraint FKAA6D673035478480 foreign key (IID) references BPEL_INSTANCE;
alter table BPEL_EVENT add constraint FKAA6D67302EDB1D12 foreign key (PID) references BPEL_PROCESS;
alter table BPEL_INSTANCE add constraint FKE1DED41FAEE24852 foreign key (INSTANTIATING_CORRELATOR) references BPEL_CORRELATOR;
alter table BPEL_INSTANCE add constraint FKE1DED41FBE166846 foreign key (LDATA_ID) references LARGE_DATA;
alter table BPEL_INSTANCE add constraint FKE1DED41F48037472 foreign key (PROCESS_ID) references BPEL_PROCESS;
alter table BPEL_MESSAGE add constraint FK4FA7231D57595642 foreign key (MEX) references BPEL_MESSAGE_EXCHANGE;
alter table BPEL_MESSAGE add constraint FK4FA7231D9541000C foreign key (DATA) references LARGE_DATA;
alter table BPEL_MESSAGE_EXCHANGE add constraint FKBDA6BD05565C81D1 foreign key (INSTANCE) references BPEL_INSTANCE;
alter table BPEL_MESSAGE_EXCHANGE add constraint FKBDA6BD056CAAD1DE foreign key (LDATA_EPR_ID) references LARGE_DATA;
alter table BPEL_MESSAGE_EXCHANGE add constraint FKBDA6BD051C6689F6 foreign key (PROCESS) references BPEL_PROCESS;
alter table BPEL_MESSAGE_EXCHANGE add constraint FKBDA6BD0565EE6160 foreign key (RESPONSE) references BPEL_MESSAGE;
alter table BPEL_MESSAGE_EXCHANGE add constraint FKBDA6BD05BB863FAE foreign key (REQUEST) references BPEL_MESSAGE;
alter table BPEL_MEX_PROPS add constraint FK203CAFC757595642 foreign key (MEX) references BPEL_MESSAGE_EXCHANGE;
alter table BPEL_PLINK_VAL add constraint FK7D71E7429CD0BFEC foreign key (MYROLE_EPR) references LARGE_DATA;
alter table BPEL_PLINK_VAL add constraint FK7D71E7426B32FC0 foreign key (SCOPE) references BPEL_SCOPE;
alter table BPEL_PLINK_VAL add constraint FK7D71E7421C6689F6 foreign key (PROCESS) references BPEL_PROCESS;
alter table BPEL_PLINK_VAL add constraint FK7D71E7427ECAB308 foreign key (PARTNERROLE_EPR) references LARGE_DATA;
alter table BPEL_PROCESS add constraint FK449418595139053 foreign key (CBP) references LARGE_DATA;
alter table BPEL_PROCESS_PROPERTY add constraint FK4690A34F48037472 foreign key (PROCESS_ID) references BPEL_PROCESS;
alter table BPEL_SCOPE add constraint FKAB2A32EA357A6E10 foreign key (PIID) references BPEL_INSTANCE;
alter table BPEL_SCOPE add constraint FKAB2A32EAB88BDC47 foreign key (PARENT_SCOPE_ID) references BPEL_SCOPE;
alter table BPEL_XML_DATA add constraint FKB7D47E7CBE166846 foreign key (LDATA_ID) references LARGE_DATA;
alter table BPEL_XML_DATA add constraint FKB7D47E7C357A6E10 foreign key (PIID) references BPEL_INSTANCE;
alter table BPEL_XML_DATA add constraint FKB7D47E7CE3239452 foreign key (SCOPE_ID) references BPEL_SCOPE;
create index IDX_MESSAGE_EXCHANGE_SYSTEM on PXE_MESSAGE_EXCHANGE (SYSTEM_ID);
alter table PXE_MESSAGE_EXCHANGE add constraint FK61DF843D5DFB8D12 foreign key (INPUT_MESSAGE_ID) references PXE_MESSAGE;
alter table PXE_MESSAGE_EXCHANGE add constraint FK61DF843D68D243D0 foreign key (LDATA_DEPR_ID) references LARGE_DATA;
alter table PXE_MESSAGE_EXCHANGE add constraint FK61DF843D824FBC9F foreign key (LDATA_SEPR_ID) references LARGE_DATA;
alter table PXE_MESSAGE_EXCHANGE add constraint FK61DF843DBE166846 foreign key (LDATA_ID) references LARGE_DATA;
alter table PXE_MESSAGE_EXCHANGE add constraint FK61DF843D59DB37B foreign key (OUTPUT_MESSAGE_ID) references PXE_MESSAGE;
alter table PXE_MESSAGE_EXCHANGE add constraint FK61DF843D45780190 foreign key (SYSTEM_ID) references PXE_SYSTEM;
create index IDX_MESSAGE_PART_MESSAGE on PXE_MESSAGE_PART (MESSAGE_ID);
alter table PXE_MESSAGE_PART add constraint FK3A07F0DAC98717D foreign key (MESSAGE_ID) references PXE_MESSAGE;
alter table PXE_MESSAGE_PART add constraint FK3A07F0DBE166846 foreign key (LDATA_ID) references LARGE_DATA;
alter table PXE_SYSTEM add constraint FK8249FD716BA4FE41 foreign key (SAR_LDATA_ID) references LARGE_DATA;
alter table PXE_SYSTEM add constraint FK8249FD71EE87D70 foreign key (DOMAIN_ID) references PXE_DOMAIN;
alter table PXE_SYSTEM add constraint FK8249FD71CE4EE6D2 foreign key (SDD_LDATA_ID) references LARGE_DATA;
alter table VAR_PROPERTY add constraint FK9C1E2C0DB377721 foreign key (XML_DATA_ID) references BPEL_XML_DATA;
create table hibernate_unique_key ( next_hi integer );
insert into hibernate_unique_key values ( 0 );
-- 
-- Apache Derby scripts by Steve Stewart.
-- Based on Srinivas Venkatarangaiah's file for Cloudscape
-- 
-- In your Quartz properties file, you'll need to set
-- org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.CloudscapeDelegate
-- 
-- Known to work with Apache Derby 10.0.2.1
-- 

create table qrtz_job_details (
job_name varchar(80) not null,
job_group varchar(80) not null,
description varchar(120) ,
job_class_name varchar(128) not null,
is_durable varchar(5) not null,
is_volatile varchar(5) not null,
is_stateful varchar(5) not null,
requests_recovery varchar(5) not null,
job_data blob,
primary key (job_name,job_group)
);

create table qrtz_job_listeners (
job_name varchar(80) not null,
job_group varchar(80) not null,
job_listener varchar(80) not null,
primary key (job_name,job_group,job_listener),
foreign key (job_name,job_group) references qrtz_job_details(job_name,job_group)
);

create table qrtz_triggers (
trigger_name varchar(80) not null,
trigger_group varchar(80) not null,
job_name varchar(80) not null,
job_group varchar(80) not null,
is_volatile varchar(5) not null,
description varchar(120) ,
next_fire_time bigint,
prev_fire_time bigint,
trigger_state varchar(16) not null,
trigger_type varchar(8) not null,
start_time bigint not null,
end_time bigint,
calendar_name varchar(80),
misfire_instr smallint,
job_data blob,
primary key (trigger_name,trigger_group),
foreign key (job_name,job_group) references qrtz_job_details(job_name,job_group)
);

create table qrtz_simple_triggers (
trigger_name varchar(80) not null,
trigger_group varchar(80) not null,
repeat_count bigint not null,
repeat_interval bigint not null,
times_triggered bigint not null,
primary key (trigger_name,trigger_group),
foreign key (trigger_name,trigger_group) references qrtz_triggers(trigger_name,trigger_group)
);

create table qrtz_cron_triggers (
trigger_name varchar(80) not null,
trigger_group varchar(80) not null,
cron_expression varchar(80) not null,
time_zone_id varchar(80),
primary key (trigger_name,trigger_group),
foreign key (trigger_name,trigger_group) references qrtz_triggers(trigger_name,trigger_group)
);

create table qrtz_blob_triggers (
trigger_name varchar(80) not null,
trigger_group varchar(80) not null,
blob_data blob ,
primary key (trigger_name,trigger_group),
foreign key (trigger_name,trigger_group) references qrtz_triggers(trigger_name,trigger_group)
);

create table qrtz_trigger_listeners (
trigger_name varchar(80) not null,
trigger_group varchar(80) not null,
trigger_listener varchar(80) not null,
primary key (trigger_name,trigger_group,trigger_listener),
foreign key (trigger_name,trigger_group) references qrtz_triggers(trigger_name,trigger_group)
);

create table qrtz_calendars (
calendar_name varchar(80) not null,
calendar blob not null,
primary key (calendar_name)
);

create table qrtz_paused_trigger_grps (
trigger_group varchar(80) not null,
primary key (trigger_group)
);

create table qrtz_fired_triggers (
entry_id varchar(95) not null,
trigger_name varchar(80) not null,
trigger_group varchar(80) not null,
is_volatile varchar(5) not null,
instance_name varchar(80) not null,
fired_time bigint not null,
state varchar(16) not null,
job_name varchar(80),
job_group varchar(80),
is_stateful varchar(5),
requests_recovery varchar(5),
primary key (entry_id)
);

create table qrtz_scheduler_state (
instance_name varchar(80) not null,
last_checkin_time bigint not null,
checkin_interval bigint not null,
recoverer varchar(80),
primary key (instance_name)
);

create table qrtz_locks (
lock_name varchar(40) not null,
primary key (lock_name)
);

insert into qrtz_locks values('TRIGGER_ACCESS');
insert into qrtz_locks values('JOB_ACCESS');
insert into qrtz_locks values('CALENDAR_ACCESS');
insert into qrtz_locks values('STATE_ACCESS');
insert into qrtz_locks values('MISFIRE_ACCESS');

