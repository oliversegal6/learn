ORACLE回滚段管理

回滚段管理一直是ORACLE数据库管理的一个难题，本文通过实例介绍ORACLE回滚段的概念，用法和规划及问题的解决。 

回滚段概述 
    回滚段用于存放数据修改之前的值（包括数据修改之前的位置和值）。回滚段的头部包含正在使用的该回滚段事务的信息。一个事务只能使用一个回滚段来存放它的回滚信息，而一个回滚段可以存放多个事务的回滚信息。 

回滚段的作用 

    事务回滚：当事务修改表中数据的时候，该数据修改前的值（即前影像）会存放在回滚段中，当用户回滚事务（ROLLBACK）时，ORACLE将会利用回滚段中的数据前影像来将修改的数据恢复到原来的值。 
    事务恢复：当事务正在处理的时候，例程失败，回滚段的信息保存在重做日志文件中，ORACLE将在下次打开数据库时利用回滚来恢复未提交的数据。 
    读一致性：当一个会话正在修改数据时，其它的会话将看不到该会话未提交的修改。而且，当一个语句正在执行时，该语句将看不到从该语句开始执行后的未提交的修改（语句级读一致性）。当ORACLE执行SELECT语句时，ORACLE依照当前的系统改变号（SYSTEM CHANGE NUMBER-SCN）来保证任何前于当前SCN的未提交的改变不被该语句处理。可以想象：当一个长时间的查询正在执行时，若其它会话改变了该查询要查询的某个数据块，ORACLE将利用回滚段的数据前影像来构造一个读一致性视图。 

事务级的读一致性 

ORACLE一般提供SQL语句级（SQL STATEMENT LEVEL）的读一致性，可以用以下语句来实现事务级的读一致性。 

SET TRANSACTION READ ONLY； 

或： 

SET TANNSACTION SERIALIZABLE； 

以上两个语句都将在事务开始后提供读一致性。需要注意的是，使用第二个语句对数据库的并发性和性能将带来影响。 

回滚段的种类 

    系统回滚段：当数据库创建后，将自动创建一个系统回滚段，该回滚段只用于存放系统表空间中对象的前影像。 
    非系统回滚段：拥有多个表空间的数据库至少应该有一个非系统回滚段，用于存放非系统表空间中对象的数据前影像。非系统回滚段又分为私有回滚段和公有回滚段，私有回滚段应在参数文件的ROLLBACK SEGMENTS参数中列出，以便例程启动时自动使其在线（ONLINE）。公有回滚段一般在OPS（ORACLE并行服务器）中出现，将在例程启动时自动在线。 
    DEFERED回滚段：该回滚段在表空间离线（OFFLINE）时由系统自动创建，当表空间再次在线（ONLINE）时由系统自动删除，用于存放表空间离线时产生的回滚信息。 

回滚段的使用 

    分配回滚段：当事务开始时，ORACLE将为该事务分配回滚段，并将拥有最少事务的回滚段分配给该事务。事务可以用以下语句申请指定的回滚段： 

SET TRANSTRACTION USE ROLLBACK SEGMENT rollback_segment 

事务将以顺序，循环的方式使用回滚段的区（EXTENTS），当当前区用满后移到下一个区。几个事务可以写在回滚段的同一个区，但每个回滚段的块只能包含一个事务的信息。 


例如（两个事务使用同一个回滚段，该回滚段有四个区）： 
1、事务在进行中，它们正在使用回滚段的第三个区； 
2、当两个事务产生更多的回滚信息，它们将继续使用第三个区； 
3、当第三个区满后，事务将写到第四个区，当事务开始写到一个新的区时，称为翻转（WRAP）； 
4、当第四个区用满时，如果第一个区是空闲或非活动（使用该区的所有事务完成而没有活动的事务）的，事务将接着使用第一个区。 

回滚段的扩张（EXTEND） 

    当当前回滚段区的所有块用完而事务还需要更多的回滚空间时，回滚段的指针将移到下一个区。当最后一个区用完，指针将移到第一个区的前面。回滚段指针移到下一个区的前提是下一个区没有活动的事务，同时指针不能跨区。当下一个区正在使用时，事务将为回滚段分配一个新的区，这种分配称为回滚段的扩展。回滚段将一直扩展到该回滚段区的个数到达回滚段的参数MAXEXTENTS的值时为止。 

回滚段的回收和OPTIMAL参数 

OPTIMAL参数指明回滚段空闲时收缩到的位置，指明回滚段的OPTIMAL参数可以减少回滚段空间的浪费。

创建回滚段 

　　语法： 

　　CREATE [PUBLIC] ROLLBACK SEGMENT rollback_segment 
　　　　 [TABLESPACE tablespace] 
　　　　 [STORAGE ([INITIAL integer[K|M]] [NEXT integer[K|M]] 
　　　　　　　　　　 [MINEXTENTS integer] 
　　　　　　　　　　 [MAXTENTS {integer|UNLIMITED}] 
　　　　　　　　　　 [OPTIMAL {integer[K|M]|NULL}]) ] 
　　注： 
　　 回滚段可以在创建时指明PRIVATE或PUBLIC，一旦创建将不能修改。 
　　 MINEXTENTS 必须大于等于2 
　　 PCTINCREASE必须是0 
　　 OPTIMAL如果要指定，必须大于等于回滚段的初始大小（由MINEXTENTS指定） 
　　建议： 

　　 一般情况下，INITIAL=NEXT 
　　 设置OPTIMAL参数来节约空间的使用 
　　 不要设置MAXEXTENTS为UNLIMITED 
　　 回滚段应创建在一个特定的回滚段表空间内 
　　例： 

　　CREATE ROLLBACK SEGMENT rbs01 
　　 TABLESPACE rbs 
　　 STORAGE ( INITIAL 100K NEXT 100K MINEXTENTS 10 
　　　　　　 MAXEXTENTS 500 OPTIMAL 1000K); 

使回滚段在线 

　　当回滚段创建后，回滚段是离线的，不能被数据库使用，为了使回滚段被事务利用，必须将回滚段在线。可以用以下命令使回滚段在线： 

　　ALTER ROLLBACK SEGMENT rollback_segment ONLINE; 

　　例： 

　　ALTER ROLLBACK SEGMENT rbs01 ONLINE； 

　　为了使回滚段在数据库启动时自动在线，可以在数据库的参数文件中列出回滚段的名字。例如在参数文件中加入以下一行： 

　　ROLLBACK_SEGMENT=(rbs01,rbs02) 

修改回滚段的存储参数 

　　可以使用ALTER ROLLBACK SEGMENT命令修改回滚段的存储参数（包括OPTIMAL，MAXEXTENTS）。 

　　语法： 

　　ALTER ROLLBACK SEGMENT rollback_segment 

　　[STORAGE ([NEXT integer[K|M]] 

　　　　　　 [MINEXTENTS integer] 

　　　　　　 [MAXEXTENTS {integer|UNLIMITED}] 

　　　　　　 [OPTIMAL {integer[K|M]|NULL}]) ] 

　　例： 

　　ALTER ROLLBACK SEGMENT rbs01 STORAGE (MAXEXTENTS 1000); 

回收回滚段的空间 

　　如果指定了回滚段的OPTIMAL参数，ORACLE将自动回收回滚段到OPTIMAL指定的位置。用户也可以手动回收回滚段的空间。 

　　语法： 
　　ALTER ROLLBACK SEGMENT rollback_segment SHRINK [TO integer [K|M]]; 
　　说明： 
　　 如果不指明TO integer的数值，ORACLE将试图回收到OPTIMAL的位置。 

　　例： 

　　ALTER ROLLBACK SEGMENT rbs01 SHRINK TO 2M; 

使回滚段离线 

　　为了达到以下两个目的将要回滚段离线： 
　　1.阻止新的事务使用该回滚段； 
　　2.该回滚段必须删除。 

　　语法： 
　　 ALTER ROLLBACK SEGMENT rollback_segment OFFLINE; 
　　例： 

　　 ALTER ROLLBACK SEGMENT rbs01 OFFLINE; 

　　说明： 

　　 如果有事务正在使用该回滚段，运行该命令后，回滚段的状态将是PENDING OFFLINE。事务结束后，状态将改为OFFLINE，可以通过V$ROLLSTAT查询回滚段的状态。 

删除回滚段 

　　当回滚段不再需要或要重建以改变INITIAL，NEXT或MINEXTENTS参数时，可以将其删除。要删除回滚段，不许使该回滚段离线。 

语法： 

　　DROP ROLLBACK SEGMENT rollback_segment; 

例： 

　　DROP ROLLBACK SEGMENT rbs01; 

查询回滚段的信息 

　　所用数据字典：DBA_ROLLBACK_SEGS 

　　可以查询的信息：回滚段的标识(SEGMENT_ID)、名称(SEGMENT_NAME)、所在表空间(TABLESPACE_NAME)、类型(OWNER)、状态(STATUS)。 

　　例： 

　　SQL>SELECT segment_name,tablespace_name,owner,status FROM dba_rollback_segs; 

回滚段的统计信息 

　　数据字典：V$ROLLNAME,V$ROLLSTAT 

　　例： 

　　SQL>SELECT n.name,s.extents,s.rssize,s.optsize,s.hwmsize,s.xacts,s.status 

　　　　FROM v$rollname n,v$rollstat s 

　　　　WHERE n.usn=s.usn; 

回滚段的当前活动事务 

　　数据字典：V$SESSION,V$TRANSACTION 

　　例： 

　　SQL>SELECT s.username,t.xidusn,t.ubafil,t.ubablk,t.used_ublk 
　　　　FROM v$session s,v$transaction t 
　　　　WHERE s.saddr=t.ses_addr; 
　　 USERNAME　　XIDUSN　　 UBAFIL　　 UBABLK　　USED_UBLK 

　　 -------　　-------- ----------- ----------- ----------- 
　　 SYSTEM　　　　　　2　　　　　　2　　　　 7　　　　　　1 
　　 SCOTT　　　　　　 1　　　　　　2　　　　163　　　　　　1 
　　 2 rows selected. 

回滚段的数量规划 

　　对于OLTP系统，存在大量的小事务处理，一般建议： 
　　数量多的小回滚段；每四个事务一个回滚段；每个回滚段不要超过十个事务。 
　　对于批处理，一般建议： 

　　少的大回滚段；每个事务一个回滚段。 

回滚段的问题及解决方法 

　　问题一：事务要求的回滚段空间不够，表现为表空间用满（ORA-01560错误），回滚段扩展到达参数MAXEXTENTS的值（ORA-01628）。 
　　解决方法：向回滚段表空间添加文件或使已有的文件变大；增加MAXEXTENTS的值。 
　　问题二：读一致性错误（ORA-01555 SNAPSHOT TOO OLD） 
　　解决方法：增加MINEXTENTS的值，增加区的大小，设置一个高的OPTIMAL值。