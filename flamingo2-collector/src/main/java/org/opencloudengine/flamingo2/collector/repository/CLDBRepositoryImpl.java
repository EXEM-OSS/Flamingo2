package org.opencloudengine.flamingo2.collector.repository;

import org.opencloudengine.flamingo2.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;

/**
 * Created by Park on 15. 6. 12..
 */
@Repository
public class CLDBRepositoryImpl implements CLDBRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void insert(String system, String name, Map<String, Object> cldbInfo) {
        Date date = new Date();
        jdbcTemplate.update("DELETE FROM FL_CL_CLDB WHERE reg_dt < ?", DateUtils.addDays(date, -7));

        jdbcTemplate.update("INSERT INTO FL_CL_CLDB (system,name,type,fileServerCount,volumeCount,replNumContainerCopied,replNumMBCopied,replSerializedSize,used,free,total,totalFiles,jvmMaxMemory,jvmTotalMemory,jvmFreeMemory,jvmUsedMemory,reg_dt,yyyy,mm,dd) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                system,
                name,
                "cldb",
                cldbInfo.get("fileServerCount"),
                cldbInfo.get("volumeCount"),
                cldbInfo.get("replNumContainerCopied"),
                cldbInfo.get("replNumMBCopied"),
                cldbInfo.get("replSerializedSize"),
                cldbInfo.get("used"),
                cldbInfo.get("free"),
                cldbInfo.get("total"),
                cldbInfo.get("totalFiles"),
                cldbInfo.get("jvmMaxMemory"),
                cldbInfo.get("jvmTotalMemory"),
                cldbInfo.get("jvmFreeMemory"),
                cldbInfo.get("jvmUsedMemory"),
                date,
                DateUtils.parseDate(date, "yyyy"),
                DateUtils.parseDate(date, "MM"),
                DateUtils.parseDate(date, "dd")
        });
    }
}