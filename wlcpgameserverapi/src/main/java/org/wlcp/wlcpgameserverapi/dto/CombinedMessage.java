package org.wlcp.wlcpgameserverapi.dto;

import java.util.ArrayList;
import java.util.List;

public class CombinedMessage {
	public List<OutputMessage> outputMessages = new ArrayList<>();
	public List<InputMessage> inputMessages = new ArrayList<>();
}
