package org.tfl.constants;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Label {
	UNCLASSIFIED("Unclassified", 1), CONFIDENTIAL("Confidential", 2), SECRET("Secret", 3), TOPSECRET("Top Secret", 4);

	private int value;
	private String name;

	Label(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	static final Map<String, Label> names = Arrays.stream(Label.values())
			.collect(Collectors.toMap(Label::getName, Function.identity()));
	static final Map<Integer, Label> values = Arrays.stream(Label.values())
			.collect(Collectors.toMap(Label::getValue, Function.identity()));

	public static Label fromName(final String name) {
		return names.get(name);
	}

	public static Label fromValue(final int value) {
		return values.get(value);
	}
}
