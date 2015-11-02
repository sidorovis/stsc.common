package stsc.common.algorithms;

import stsc.common.Settings;

/**
 * This is helper for creating output algorithm names. <br/>
 * Any simulation could have special 'output' algorithms (instances of ${link stsc.algorithms.Output}). <br/>
 * This algorithm - returns could be used for displaying data on charts.
 */
public final class AlgorithmNameGenerator {

	private AlgorithmNameGenerator() {
	}

	public static String generateOutAlgorithmName(final String baseAlgorithmName) {
		return baseAlgorithmName + Settings.algorithmStaticPostfix;
	}

}
