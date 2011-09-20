package com.orbitz.monitoring.lib.processor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.orbitz.monitoring.api.Monitor;
import com.orbitz.monitoring.lib.processor.KestrelClient.ParseError;
import com.orbitz.monitoring.lib.renderer.MonitorRenderer;

public class KestrelMonitorProcessorTest {

	KestrelClient client;
	MonitorRenderer renderer;

	@Before
	public void setUp() {
		client = mock(KestrelClient.class);
		renderer = mock(MonitorRenderer.class);
	}
	
	@After
	public void tearDown() {
		verifyNoMoreInteractions(renderer);
		verifyNoMoreInteractions(client);
	}

	@Test
	public void shutdownClosesClinet() throws IOException {
		KestrelMonitorProcessor processor = new KestrelMonitorProcessor(client, renderer);
		processor.shutdown();
		verify(client).close();
	}

	@Test
	public void processQueues() throws IOException, ParseError {
		Monitor monitor = mock(Monitor.class);
		String monitorString = RandomStringUtils.random(100);

		when(renderer.renderMonitor(monitor)).thenReturn(monitorString);
		when(client.queue("ERMA", monitorString)).thenReturn(true);

		KestrelMonitorProcessor processor = new KestrelMonitorProcessor(client, renderer);

		processor.process(monitor);
		
		verify(renderer).renderMonitor(monitor);
		verify(client).queue("ERMA", monitorString);
	}

}
