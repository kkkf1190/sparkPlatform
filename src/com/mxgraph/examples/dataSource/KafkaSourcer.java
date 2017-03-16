package com.mxgraph.examples.dataSource;

public class KafkaSourcer implements SourcerInterface{
	String address;
	String group;
	String topic;
	public KafkaSourcer(String type){
		String[] params = type.split("-");
		this.address = params[1];
		this.group = params[2];
		this.topic = params[3];
		
	}

	@Override
	public String getSourceCode() {
		// TODO Auto-generated method stub
		String ans = "val topicMap = \""+this.topic+"\".split(\",\").map((_, numThreads.toInt)).toMap\n"
		+"val lines = KafkaUtils.createStream(ssc, \""+this.address+"\", \""+this.group+"\", topicMap).map(_._2)";
		return ans;
	}

}
