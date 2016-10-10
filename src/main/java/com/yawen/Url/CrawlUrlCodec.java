package com.yawen.Url;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import com.yawen.Url.CrawlUrl;

public class CrawlUrlCodec implements Codec<CrawlUrl>{
	public void encode(BsonWriter writer, CrawlUrl crawlUrl, EncoderContext encoderContext) {
		// TODO Auto-generated method stub
		writer.writeStartDocument();
		writer.writeString("anchor",crawlUrl.getAnchor());
		writer.writeString("url",crawlUrl.getUrl());
		writer.writeString("partenturl",crawlUrl.getParenturl());
		writer.writeDouble("cash",crawlUrl.getCash());
		writer.writeString("tag",crawlUrl.getTag());
		writer.writeInt32("depth",crawlUrl.getDepth());
		
	
		writer.writeEndDocument();
		
	}
	public Class<CrawlUrl> getEncoderClass() {
		// TODO Auto-generated method stub
		return CrawlUrl.class;
	}
	public CrawlUrl decode(BsonReader reader, DecoderContext decoderContext) {
		// TODO Auto-generated method stub
		CrawlUrl crawlUrl=new CrawlUrl();
		reader.readStartDocument();
		reader.readObjectId("_id");
		crawlUrl.setAnchor(reader.readString("anchor"));
		crawlUrl.setUrl(reader.readString("url"));
		crawlUrl.setParenturl(reader.readString("parenturl"));
		crawlUrl.setCash(reader.readDouble("cash"));
		
		crawlUrl.setTag(reader.readString("tag"));
		crawlUrl.setDepth(reader.readInt32("depth"));
		reader.readEndDocument();
	
		return crawlUrl;
	}
}
