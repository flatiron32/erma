package com.orbitz.monitoring.lib.processor;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.orbitz.monitoring.api.Monitor;
import com.orbitz.monitoring.lib.processor.KestrelClient.ParseError;
import com.orbitz.monitoring.lib.renderer.MonitorRenderer;
import com.orbitz.monitoring.lib.renderer.SimpleMonitorRenderer;

public class KestrelMonitorProcessor extends MonitorProcessorAdapter {
	private static final Logger LOGGER = Logger.getLogger(KestrelMonitorProcessor.class); 
	
	private KestrelClient client;
	private MonitorRenderer monitorRenderer;
	
	public KestrelMonitorProcessor(String hostname, int port) throws IOException {
		this(new KestrelClient(hostname, port), new SimpleMonitorRenderer());
	}
	
	protected KestrelMonitorProcessor(KestrelClient client, MonitorRenderer monitorRenderer) {
		this.client = client;
		this.monitorRenderer = monitorRenderer;
	}

	@Override
	public void shutdown() {
		try {
			client.close();
		} catch (IOException ioe) {
			LOGGER.error("Unable to close Kestrel Client.", ioe);
		}
	}
	
	@Override
	public void process(Monitor monitor) {
		try {
			boolean response = client.queue("ERMA", monitorRenderer.renderMonitor(monitor));
			if (!response) {
				LOGGER.warn(monitor.get(Monitor.NAME) + " not put");
			} 
		} catch (ParseError pe) {
			LOGGER.error("Unable to parse Kestrel response.  Data MIGHT have made it.", pe);
		} catch (IOException ioe) {
			LOGGER.error("Unable to send to Kestrel.", ioe);
		}
	}
}
