package org.opencloudengine.flamingo2.util;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class SystemUtils {

    public static final long MEGA_BYTES = 1024 * 1024;

    public static String getPid() {
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            if (name != null) {
                return name.split("@")[0];
            }
        } catch (Throwable ex) {
            // Ignore
        }
        return "????";
    }

    public static Map getSystemMetrics(SigarProxy sigar) throws UnknownHostException, SigarException {
        final Runtime rt = Runtime.getRuntime();
        final long maxMemory = rt.maxMemory() / MEGA_BYTES;
        final long totalMemory = rt.totalMemory() / MEGA_BYTES;
        final long freeMemory = rt.freeMemory() / MEGA_BYTES;
        final long usedMemory = totalMemory - freeMemory;

        InetAddress address = InetAddress.getLocalHost();
        CpuPerc cpuPerc = sigar.getCpuPerc();
        ProcCpu procCpu = sigar.getProcCpu(SystemUtils.getPid());

        HashMap map = new HashMap();
        map.put("type", "web");
        map.put("cpu-idle", (int) cpuPerc.getIdle() * 100);
        map.put("cpu-sys", (int) cpuPerc.getSys() * 100);
        map.put("cpu-user", (int) cpuPerc.getUser() * 100);
        map.put("hostname", address.getHostName());
        map.put("ip", address.getHostAddress());
        map.put("proc-cpu-user", procCpu.getUser());
        map.put("proc-cpu-sys", procCpu.getSys());
        map.put("proc-cpu-total", procCpu.getTotal());
        map.put("proc-cpu-per", procCpu.getPercent());
        map.put("heap-total", totalMemory);
        map.put("heap-used", usedMemory);
        map.put("heap-free", freeMemory);
        map.put("heap-max", maxMemory);
        return map;
    }

}
