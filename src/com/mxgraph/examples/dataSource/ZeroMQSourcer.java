package com.mxgraph.examples.dataSource;

public class ZeroMQSourcer implements SourcerInterface{
	String address;
	String topic;
	public ZeroMQSourcer(String type){
		String[] param = type.split("-");
		this.address = param[1];
		this.topic = param[2];
	}

	@Override
	public String getSourceCode() {
		// TODO Auto-generated method stub
		String ans = "val url=\""+this.address+"\"\n val topic = \""+this.topic+"\"\n"
				+ "def bytesToStringIterator(x: Seq[ByteString]): Iterator[String] = x.map(_.utf8String).iterator"
						+"val lines = ZeroMQUtils.createStream(ssc, url, Subscribe(topic), bytesToStringIterator _)";
		return ans;
	}

}
