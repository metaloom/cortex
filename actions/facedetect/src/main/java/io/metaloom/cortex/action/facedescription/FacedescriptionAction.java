package io.metaloom.cortex.action.facedescription;

import static io.metaloom.cortex.action.facedetect.FacedetectMedia.FACE_DETECTION;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel.OllamaChatModelBuilder;
import io.metaloom.ai.genai.llm.LargeLanguageModel;
import io.metaloom.ai.genai.utils.TextUtils;
import io.metaloom.cortex.action.facedetect.FacedetectActionOptions;
import io.metaloom.cortex.action.facedetect.FacedetectMedia;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.cortex.action.facedetect.avro.Facedetection;
import io.metaloom.loom.rest.model.asset.AssetResponse;
import io.metaloom.video4j.utils.ImageUtils;

@Singleton
public class FacedescriptionAction extends AbstractMediaAction<FacedetectActionOptions> {

	private static final Logger logger = LoggerFactory.getLogger(FacedescriptionAction.class);

	private static final LargeLanguageModel MODEL = FaceDescriptionModel.OLLAMA_GEMMA3_12B_Q8;

	public static final String PROMPT = """
		Describe the face. Output only valid JSON without wrapper.
		Example:

		{
		  "nsfw": true|false,
		  "eyes_status": "open|closed",
		  "hair_color":"blue|brown|green|unknown",
		  "mouth_status": "smile|frown|funny|scream|closed|neutral",
		  "face_age": 18,
		  "face_gender":  "male|female",
		  "face_race": "caucasian|black|asian|latin",
		  "face_occluded_by": "",
		  "face_occluded": true|false,
		  "face_frontal_view": true|false,
		  "face_profile_view": true|false,
		}
		""";

	public static final String URL = "http://127.0.0.1:11434";

	private ObjectMapper mapper;

	@Inject
	public FacedescriptionAction(@Nullable LoomClient client, CortexOptions cortexOption, FacedetectActionOptions options, ObjectMapper mapper) {
		super(client, cortexOption, options);
		this.mapper = mapper;
	}

	@Override
	public String name() {
		return "facedescription";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		LoomMedia media = ctx.media();
		return media.isVideo() || media.isImage();
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		FacedetectMedia media = ctx.media(FACE_DETECTION);
		// TODO check the flags with the options to figure out whether we need to rerun the detection
		return media.hasFacedetectionFlag();
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws IOException {
		LoomMedia media = ctx.media();
		if (media.isVideo() || media.isImage()) {
			return processFaces(ctx);
		} else {
			return ctx.skipped("No visual media").next();
		}
	}

	private ActionResult processFaces(ActionContext ctx) {
		FacedetectMedia media = ctx.media(FACE_DETECTION);
		Integer count = media.getFaceCount();
		if(count!=null && count>0) {
			for(Facedetection detection : media.getFacedetections()) {
				detection.getThumbnail();
			}
		}
		return null;
	}

	public FaceDescription processFace(BufferedImage image) throws IOException {

		OllamaChatModelBuilder builder = OllamaChatModel.builder()
			.baseUrl(URL)
			.timeout(Duration.ofMinutes(15))
			.modelName(MODEL.id())
			.numPredict(2048)
			.temperature(0.6);
		OllamaChatModel chat = builder.build();

		String base64 = ImageUtils.toBase64JPG(image);
		TextContent q = TextContent.from(PROMPT);
		ImageContent img = ImageContent.from(base64, "image/jpeg");
		ChatMessage msg = UserMessage.from(q, img);
		String json = null;
		for (int i = 0; i < 3; i++) {
			try {
				ChatResponse out = chat.chat(msg);
				json = out.aiMessage().text();
				json = TextUtils.extractJson(json);
				FaceDescription description = mapper.readValue(json, FaceDescription.class);
				return description;
			} catch (JsonParseException e1) {
				logger.error("Failed to parse LLM response:\n" + json, e1);
			} catch (Exception e) {
				logger.error("Failed to process image", e);
			}
		}
		return null;
	}
}
