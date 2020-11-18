package com.dinghai.raft.service;

import com.dinghai.raft.proto.RaftMessage;

public interface RaftElectionService {
    public RaftMessage.VoteResponse vote(RaftMessage.VoteRequest voteRequest);
    public RaftMessage.AppendEntityResponse appendEntities(RaftMessage.AppendEntityRequest appendEntityRequest);
}
