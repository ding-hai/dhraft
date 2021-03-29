package com.dinghai.raft.core.service;

import com.baidu.brpc.client.RpcCallback;
import com.dinghai.raft.core.proto.RaftMessage;

import java.util.concurrent.Future;

public interface RaftConsensusServiceAsync extends RaftConsensusService {
    Future<RaftMessage.VoteResponse> vote(RaftMessage.VoteRequest voteRequest, RpcCallback<RaftMessage.VoteResponse> callback);
    Future<RaftMessage.AppendEntityResponse> appendEntities(RaftMessage.AppendEntityRequest appendEntityRequest, RpcCallback<RaftMessage.AppendEntityResponse> callback);
}
