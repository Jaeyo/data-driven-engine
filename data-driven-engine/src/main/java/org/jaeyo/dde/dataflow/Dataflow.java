package org.jaeyo.dde.dataflow;


import java.util.Set;

import javax.inject.Singleton;

import org.jaeyo.dde.dataflow.component.Component;

import com.google.common.collect.Sets;

@Singleton
public class Dataflow{
	private Set<Component> components = Sets.newHashSet();
} //class