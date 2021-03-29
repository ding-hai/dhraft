package com.dinghai.raft.core.service;

import com.dinghai.raft.core.proto.RaftMessage;

public interface RaftConsensusService {
    public RaftMessage.VoteResponse vote(RaftMessage.VoteRequest voteRequest);
    public RaftMessage.AppendEntityResponse appendEntities(RaftMessage.AppendEntityRequest appendEntityRequest);
}
