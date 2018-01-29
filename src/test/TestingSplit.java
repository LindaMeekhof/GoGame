package test;

import general.Protocol.General;

public class TestingSplit {

	public static void main(String[] args) {
		String inputUser = "Indentity|HELLO$12$test$2_3$testing";
		String[] words = inputUser.split("\\" + "|");
		String[] input = words[1].split("\\" + "$");
		
		System.out.println(words[0]);
		System.out.println(words[1]);
		System.out.println(input[0]);
		
		
		
//		System.out.println(words[1]);
//		System.out.println(words.length);
//		String[] input = words[3].split("_");
//		
//		System.out.println(input[1]);
//		System.out.println(input2[0]);
//		System.out.println(input2[1]);
	}
}
