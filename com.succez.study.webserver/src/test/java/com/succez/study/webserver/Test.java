package com.succez.study.webserver;

public class Test extends TestF{
	
	public void run(){
		super.run();
		System.out.println("son thread");
	}

	public static void main(String args[]){
		Test test = new Test();
		test.start();
	}
}
