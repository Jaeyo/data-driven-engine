package org.jaeyo.dde.dataflow.component;

import org.jaeyo.dde.processor.OutProcessor;

public class OutProcessorComponent extends Component{
	private OutProcessor processor;
	
	public OutProcessorComponent(OutProcessor processor, int x, int y) {
		super(x, y);
		this.processor = processor;
	} //INIT

	public OutProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(OutProcessor processor) {
		this.processor = processor;
	}
} //class