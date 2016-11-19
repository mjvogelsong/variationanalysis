package org.campagnelab.dl.somatic.intermediaries;

import org.junit.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;

/**
 * Test the mutator on some specific examples.
 * Created by fac2003 on 5/27/16.
 */
public class MutationDirectionTest {
    @Test
    public void somaticFrequency() {
        double delta = 0.75;
        FirstSimulationStrategy strategy = new FirstSimulationStrategy(delta, delta, 0.1, 1);
        FirstSimulationStrategy.mutationDirection dir = null;

        dir = strategy.dirFromCounts(new int[]{50, 0, 0, 50});
        assertEquals(delta * 50 / (50 + 50), dir.somaticFrequency, "somaticFrequency must match");

        dir = strategy.dirFromCounts(new int[]{80, 20, 0, 0});
        assertEquals(delta * 80 / (80 + 20), dir.somaticFrequency, "somaticFrequency must match");

        dir = strategy.dirFromCounts(new int[]{80, 20, 3, 1});
        assertEquals(delta * 80 / (80 + 20 + 3 + 1), dir.somaticFrequency, "somaticFrequency must match");

        dir = strategy.dirFromCounts(new int[]{90, 1, 1, 1});
        assertEquals(delta * 90 / (90 + 1 + 1 + 1), dir.somaticFrequency, "somaticFrequency must match");

        dir = strategy.dirFromCounts(new int[]{0, 3, 1, 100});
        assertEquals(delta * 100 / (100 + 3 + 1), dir.somaticFrequency, "somaticFrequency must match");
    }

    @Test
    public void mutateTest() throws Exception {
        FirstSimulationStrategy strategy = new FirstSimulationStrategy(1, 1, 0.1, 1);
        final int mostCount = 100;
        final int secondMostCount = 12;
        //homozygous first:
        for (int numGenos = 5; numGenos < 8; numGenos++) {
            for (int maxIndex = 0; maxIndex < numGenos; maxIndex++) {
                if (maxIndex == 4) {
                    continue;
                }
                int[] counts = new int[numGenos];
                counts[maxIndex] = mostCount;
                for (int trial = 0; trial < 20; trial++) {
                    FirstSimulationStrategy.mutationDirection dir = strategy.dirFromCounts(counts);
                    System.out.println(Arrays.toString(counts) + " " + dir.oldBase + " -> " + dir.newBase);
                    assert (dir.newBase != dir.oldBase);
                    assert (dir.newBase != maxIndex);
                    assert (dir.newBase != 4);
                    assert (dir.newBase < numGenos);
                }
            }
        }
        System.out.println("\nHeterozygous:");
        //now heterozygous
        for (int numGenos = 5; numGenos < 8; numGenos++) {
            for (int maxIndex = 0; maxIndex < numGenos; maxIndex++) {
                if (maxIndex == 4) {
                    continue;
                }
                for (int secondMostIndex = 0; secondMostIndex < numGenos; secondMostIndex++) {
                    if (secondMostIndex == maxIndex || secondMostIndex == 4) {
                        continue;
                    }
                    int[] counts = new int[numGenos];
                    counts[maxIndex] = mostCount;
                    counts[secondMostIndex] = secondMostCount;
                    for (int trial = 0; trial < 20; trial++) {
                        FirstSimulationStrategy.mutationDirection dir = strategy.dirFromCounts(counts);
                        System.out.println(Arrays.toString(counts) + " " + dir.oldBase + " -> " + dir.newBase);
                        assert (dir.newBase != dir.oldBase);
                        assert (dir.newBase != maxIndex);
                        assert (dir.newBase != 4);
                        assert (dir.newBase != secondMostIndex);
                        assert (dir.newBase < numGenos);
                    }
                }

            }
        }

    }


}