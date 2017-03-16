package com.mxgraph.examples.dataSource;

public class MQTTSourcer implements SourcerInterface{
	String address;
	String topic;
	public MQTTSourcer(String type){
		String[] param = type.split("-");
		this.address = param[1];
		this.topic = param[2];
		
	}

	@Override
	public String getSourceCode() {
		// TODO Auto-generated method stub
		String ans = "val lines = MQTTUtils.createStream(ssc, \""+this.address+"\", \""+this.topic+"\", StorageLevel.MEMORY_AND_DISK_SER)";
		return ans;
	}

}
