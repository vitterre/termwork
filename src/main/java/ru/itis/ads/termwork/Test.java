package ru.itis.ads.termwork;

public class Test {
	public static int ITERATIONS = 0;

	public void doSomething() {
		for (int i = 0; i < 20; i++) {
			A a = new A();
			a.doSomething();
		}
	}

	static class A {
		public void doSomething() {
			a();
		}
		private void a() {
			for (int i = 0; i < 20; i++) {
				ITERATIONS++;
			}
		}

	}
}
