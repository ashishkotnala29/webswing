package org.webswing.server.services.stats;

import java.util.Map;

public interface StatisticsLogger {

	public static final String INBOUND_SIZE_METRIC = "inboundSize";
	public static final String OUTBOUND_SIZE_METRIC = "outboundSize";
	public static final String MEMORY_ALLOCATED_METRIC = "memoryAllocated";
	public static final String MEMORY_USED_METRIC = "memoryUsed";
	public static final String LATENCY_SERVER_RENDERING = "latencyServerRendering";
	public static final String LATENCY_CLIENT_RENDERING = "latencyClientRendering";
	public static final String LATENCY_NETWORK = "latencyNetwork";
	public static final String LATENCY= "latency";

	void log(String instance, String name, Number value);

	Map<String, Map<Long, Number>> getSummaryStats();

	Map<String, Map<Long, Number>> getInstanceStats(String instance);

	Map<String, Number> getInstanceMetrics(String clientId);

	void removeInstance(String instance);

}
