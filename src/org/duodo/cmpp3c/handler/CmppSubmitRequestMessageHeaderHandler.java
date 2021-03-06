/**
 * 
 */
package org.duodo.cmpp3c.handler;

import org.duodo.cmpp3c.message.CmppSubmitRequestMessage;
import org.duodo.cmpp3c.packet.CmppPacketType;
import org.duodo.netty3ext.message.Message;
import org.duodo.netty3ext.packet.PacketType;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * @author huzorro(huzorro@gmail.com)
 *
 */
public class CmppSubmitRequestMessageHeaderHandler extends OneToOneEncoder {
	private PacketType packetType;
	/**
	 * 
	 */
	public CmppSubmitRequestMessageHeaderHandler() {
		this(CmppPacketType.CMPPSUBMITREQUEST);
	}
	public CmppSubmitRequestMessageHeaderHandler(PacketType packetType) {
		this.packetType = packetType;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.handler.codec.oneone.OneToOneEncoder#encode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, java.lang.Object)
	 */
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
	    if(!(msg instanceof Message)) return msg;
		Message message = (Message) msg;
        long commandId = ((Long) message.getHeader().getCommandId()).longValue();
        if(commandId != packetType.getCommandId()) return msg;
        
        if(message.getPacketType().getPacketStructures()[0].isFixPacketLength()) {
        	return msg;
        }        
        
        CmppSubmitRequestMessage requestMessage = (CmppSubmitRequestMessage) msg;
        
        message.getHeader().setBodyLength(message.getHeader().getBodyLength() + requestMessage.getMsgLength());
        message.getHeader().setPacketLength(message.getHeader().getPacketLength() + requestMessage.getMsgLength());
        
        return message;
	}

}
