package dev.linkedlogics.jdbc.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.linkedlogics.context.ContextError;
import dev.linkedlogics.context.Status;
import dev.linkedlogics.service.LogicService;

public class ContextEntity {
	private String id;
	private String key;
	private String parentId;
	private Status status = Status.INITIAL;
	private int version;
	
	private String processId;
	private int processVersion;
	
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	private OffsetDateTime finishedAt;
	private OffsetDateTime expiredAt;
	
	private String data;
}
