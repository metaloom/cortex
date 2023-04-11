package io.metaloom.loom.worker.action.facedetect.cluster;

import org.apache.commons.math3.ml.clustering.DoublePoint;

import io.metaloom.utils.ConvertUtils;
import io.metaloom.video.facedetect.Face;

public class ClusterFacePoint extends DoublePoint {

	private static final long serialVersionUID = 1L;

	private Face face;

	public ClusterFacePoint(Face face) {
		super(ConvertUtils.convertFloatsToDoubles(face.getEmbeddings()));
		this.face = face;
	}

	public Face getFace() {
		return face;
	}

}
