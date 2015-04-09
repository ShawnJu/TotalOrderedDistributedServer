package main;

import java.util.concurrent.ConcurrentHashMap;

public class Storage {
	public static int AccIDCounter = 0;
	public static ConcurrentHashMap<Integer, Account> dataTable = new ConcurrentHashMap<Integer, Account>();
}