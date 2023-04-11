package io.metaloom.loom.worker.action.facedetect.cluster;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

import io.metaloom.video.facedetect.Face;

public final class FaceClusterer {

	private FaceClusterer() {
	}

	/**
	 * Cluster the faces using the density-based spatial clustering of applications with noise algorithm.
	 * 
	 * @param faces
	 *            Faces to be used for clustering
	 * @param eps
	 *            maximum radius of the neighborhood to be considered
	 * @param minPts
	 *            minimum number of points needed for a cluster
	 * @return Result which contains the resulting cluster data
	 */
	public static ClusterResult clusterFaces(List<Face> faces, float eps, int minPts) {
		// Prepare and feed the clusterer
		DBSCANClusterer<ClusterFacePoint> clusterAlgo = new DBSCANClusterer<>(eps, 2);
		List<ClusterFacePoint> ar = new ArrayList<>();
		faces.stream().forEach(face -> {
			ar.add(new ClusterFacePoint(face));
		});
		List<Cluster<ClusterFacePoint>> clusters = clusterAlgo.cluster(ar);

		// Extract the resulting data
		ClusterResult result = new ClusterResult();
		for (Cluster<ClusterFacePoint> cluster : clusters) {
			FaceCluster faceCluster = new FaceCluster();
			for (ClusterFacePoint clusterFace : cluster.getPoints()) {
				faceCluster.add(clusterFace.getFace());
			}
			result.add(faceCluster);
		}
		return result;
	}
}
