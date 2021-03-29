package com.dinghai.raft.core;

import com.baidu.brpc.client.BrpcProxy;
import com.baidu.brpc.client.RpcClient;
import com.baidu.brpc.client.channel.Endpoint;
import com.dinghai.raft.core.proto.RaftMessage;
import com.dinghai.raft.core.service.RaftConsensusService;
import com.dinghai.raft.core.service.RaftConsensusServiceAsync;

/**
 * 维护节点信息，不负责具体交互
 * 每一个RaftPeer对应一个server的ip和端口以及和这个server通信的rpc client和service
 */
public class RaftPeer {
    private RaftMessage.Server server; //当前peer对应的server信息

    private RpcClient rpcClient; // 负责和当前peer对应的server交互的rpc client
    private RaftConsensusService raftConsensusService; // 同步调用的service
    private RaftConsensusServiceAsync raftConsensusServiceAsync; // 异步调用的service

    private long matchIndex;
    private long nextIndex;

    public RaftPeer(RaftMessage.Server server) {
        this.server = server;
        this.rpcClient = new RpcClient(new Endpoint(
                server.getEndpoint().getHost(),
                server.getEndpoint().getPort()));
        this.raftConsensusService = BrpcProxy.getProxy(this.rpcClient, RaftConsensusService.class);
    }

    public RaftMessage.Server getServer() {
        return server;
    }

    public void setServer(RaftMessage.Server server) {
        this.server = server;
    }

    public RpcClient getRpcClient() {
        return rpcClient;
    }

    public void setRpcClient(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public RaftConsensusService getRaftConsensusService() {
        return raftConsensusService;
    }

    public void setRaftConsensusService(RaftConsensusService raftConsensusService) {
        this.raftConsensusService = raftConsensusService;
    }

    public RaftConsensusServiceAsync getRaftConsensusServiceAsync() {
        return raftConsensusServiceAsync;
    }

    public void setRaftConsensusServiceAsync(RaftConsensusServiceAsync raftConsensusServiceAsync) {
        this.raftConsensusServiceAsync = raftConsensusServiceAsync;
    }

    public long getMatchIndex() {
        return matchIndex;
    }

    public void setMatchIndex(long matchIndex) {
        this.matchIndex = matchIndex;
    }

    public long getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(long nextIndex) {
        this.nextIndex = nextIndex;
    }
}
