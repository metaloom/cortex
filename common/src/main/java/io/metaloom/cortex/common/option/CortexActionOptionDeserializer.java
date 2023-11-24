package io.metaloom.cortex.common.option;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.metaloom.cortex.api.option.action.CortexActionOptions;

@Singleton
public class CortexActionOptionDeserializer extends JsonDeserializer<CortexActionOptions> {

	private final static Logger log = LoggerFactory.getLogger(CortexActionOptionDeserializer.class);

	private Map<String, Class<? extends CortexActionOptions>> infoMap;

	@Inject
	public CortexActionOptionDeserializer(Set<CortexActionOptionDeserializerInfo> infos) {
		this.infoMap = toMap(infos);
	}

	@Override
	public CortexActionOptions deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
		ObjectCodec oc = jsonParser.getCodec();
		ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);
		String key = jsonParser.getCurrentName();
		Class<? extends CortexActionOptions> mappingClazz = infoMap.get(key);
		if (mappingClazz == null) {
			log.warn("Did not find module options class for mapping {}. Ignoring found option.", key);
			return null;
		} else {
			log.info("Mapping action options for " + key + " to " + mappingClazz.getSimpleName());
			return mapper.convertValue(node, mappingClazz);
		}

	}

	private Map<String, Class<? extends CortexActionOptions>> toMap(Set<CortexActionOptionDeserializerInfo> infos) {
		Map<String, Class<? extends CortexActionOptions>> map = new HashMap<>();
		for (CortexActionOptionDeserializerInfo info : infos) {
			Class<? extends CortexActionOptions> prev = map.put(info.getOptionPrefix(), info.getOptionClazz());
			if (prev != null) {
				log.error("Conflicting configuration mapping detected for prefix {} with {} and {}. Ignoring found options.", info.getOptionPrefix(),
					info.getOptionClazz().getSimpleName(), prev.getSimpleName());
				throw new RuntimeException("Invalid configuration mapping");
			}
		}
		return map;
	}
}
