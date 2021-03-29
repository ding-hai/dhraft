package com.dinghai.raft.core;

import com.dinghai.raft.core.proto.RaftMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负责具体的交互过程
 */
public class RaftNode {

    public enum State {
        FOLLOWER,
        CANDIDATE,
        LEADER
    }

    private RaftMessage.Configuration configuration;
    private final RaftMessage.Server localServer; //当前节点对应的server信息
    private Map<Integer, RaftPeer> peerMap = new HashMap<>(); // id->peer. peerMap维护的是除了当前这个server之前的其他server对应的peer

    private State state = State.FOLLOWER;

    private int leadId;
    private int voteFor;

    private long currentTerm;
    private long commitIndex;
    private long lastAppliedIndex;

    public RaftNode(List<RaftMessage.Server> servers, RaftMessage.Server localServer) {
        RaftMessage.Configuration.Builder configurationBuilder = RaftMessage.Configuration.newBuilder();
        configurationBuilder.addAllServers(servers);
        this.configuration = configurationBuilder.build();
        this.localServer = localServer;
    }

    public void init() {
        List<RaftMessage.Server> serverList = configuration.getServersList();
        for (RaftMessage.Server server : serverList) {
            if(localServer.equals(server)){
                continue;
            }
            RaftPeer peer = new RaftPeer(server);
            peerMap.put(server.getServerId(), peer);
        }
    }


}
