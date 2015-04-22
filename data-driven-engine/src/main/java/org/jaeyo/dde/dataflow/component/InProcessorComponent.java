package org.jaeyo.dde.dataflow.component;

import org.jaeyo.dde.processor.InProcessor;

public class InProcessorComponent extends Component{
	private InProcessor processor;
	
	public InProcessorComponent(InProcessor processor, int x, int y) {
		super(x, y);
		this.processor = processor;
	} //INIT
	
	public InProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(InProcessor processor) {
		this.processor = processor;
	}
} //class