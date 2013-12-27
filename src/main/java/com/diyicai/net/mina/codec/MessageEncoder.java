package com.diyicai.net.mina.codec;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 13-12-25
 * Time: 下午4:57
 * To change this template use File | Settings | File Templates.
 */
public class MessageEncoder extends ProtocolEncoderAdapter {

    private final static Logger logger = Logger.getLogger(MessageEncoder.class);

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        logger.info("encoding");
    }

}
