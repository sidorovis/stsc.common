package stsc.common.algorithms;

import stsc.common.Settings;

public final class AlgorithmNameGenerator {

	private AlgorithmNameGenerator() {

	}

	public static String generateOutAlgorithmName(final String baseAlgorithmName) {
		return baseAlgorithmName + Settings.algorithmStaticPostfix;
	}

}
