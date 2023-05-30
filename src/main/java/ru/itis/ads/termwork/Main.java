package ru.itis.ads.termwork;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class IntegerComparable implements Comparable<IntegerComparable> {
	private Integer value;

	public IntegerComparable(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public int compareTo(IntegerComparable other) {
		return this.value.compareTo(other.getValue());
	}
}

public class Main {
	public static void main(String[] args) throws IOException {
		int[] arr = Files.readAllLines(Paths.get("src/main/resources/data.csv"))
						.stream()
						.mapToInt(Integer::valueOf)
						.toArray();

		long[] timesForInsertions = new long[10000];
		long[] timesForRemoving = new long[10000];
		long[] timesForFinding = new long[10000];

		int[] stepsForInsertions = new int[10000];
		int[] stepsForRemoving = new int[10000];
		int[] stepsForFinding = new int[10000];

		FibonacciHeap fibonacciHeap = new FibonacciHeap();

		for (int i = 0; i < arr.length; i++) {
			long start = System.nanoTime();

			fibonacciHeap.insert(arr[i], new IntegerComparable(arr[i]));

			long end = System.nanoTime();
			timesForInsertions[i] = end - start;
			stepsForInsertions[i] = FibonacciHeap.ITERATIONS;
			FibonacciHeap.ITERATIONS = 0;
		}

		System.out.println(Arrays.toString(stepsForInsertions));

		for (int i = 0; i < arr.length; i++) {
			long start = System.nanoTime();

			fibonacciHeap.delete(new FibonacciHeap.Node(arr[i], new IntegerComparable(arr[i])));

			long end = System.nanoTime();
			timesForRemoving[i] = end - start;
			stepsForRemoving[i] = FibonacciHeap.ITERATIONS;
			FibonacciHeap.ITERATIONS = 0;
		}

		System.out.println(Arrays.toString(stepsForRemoving));

		for (int i = 0; i < arr.length; i++) {
			fibonacciHeap.insert(arr[i], new IntegerComparable(arr[i]));
		}

		FibonacciHeap.ITERATIONS = 0;

		for (int i = 0; i < arr.length; i++) {
			long start = System.nanoTime();

			fibonacciHeap.min();
			fibonacciHeap.removeMin();

			long end = System.nanoTime();
			timesForFinding[i] = end - start;
			stepsForFinding[i] = FibonacciHeap.ITERATIONS;
			FibonacciHeap.ITERATIONS = 0;
		}

		System.out.println(Arrays.toString(stepsForFinding));

		log(timesForInsertions, "times_for_insertions.txt");
		log(timesForRemoving, "times_for_removing.txt");
		log(timesForFinding, "times_for_finding.txt");

		log(stepsForInsertions, "steps_for_insertions.txt");
		log(stepsForRemoving, "steps_for_removing.txt");
		log(stepsForFinding, "steps_for_finding.txt");
	}

	private static void log(long[] array, String fileName) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
		for (long el : array) {
			bufferedWriter.write(Long.toString(el));
			bufferedWriter.newLine();
		}
	}

	private static void log(int[] array, String fileName) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
		for (int el : array) {
			bufferedWriter.write(Long.toString(el));
			bufferedWriter.newLine();
		}
	}
}