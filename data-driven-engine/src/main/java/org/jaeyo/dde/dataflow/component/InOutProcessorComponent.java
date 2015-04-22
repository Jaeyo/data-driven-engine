package org.jaeyo.dde.dataflow.component;

import org.jaeyo.dde.processor.InOutProcessor;

public class InOutProcessorComponent extends Component{
	private InOutProcessor processor;

	public InOutProcessorComponent(InOutProcessor processor, int x, int y) {
		super(x, y);
		this.processor = processor;
	} //INIT

	public InOutProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(InOutProcessor processor) {
		this.processor = processor;
	}
} //class