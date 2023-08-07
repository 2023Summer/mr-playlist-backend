package summer.mrplaylist.logback;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogbackTest {

	@Test
	public void logbackTest_IWE() throws Exception {
		log.trace("Trace Level 테스트");
		log.debug("DEBUG Level 테스트");
		log.info("INFO Level 테스트");
		log.warn("Warn Level 테스트");
		log.error("ERROR Level 테스트");
	}
}
