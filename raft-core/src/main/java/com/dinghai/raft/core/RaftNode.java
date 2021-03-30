package com.dinghai.raft.core;

import com.baidu.brpc.client.RpcCallback;
import com.dinghai.raft.core.proto.RaftMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 负责具体的交互过程
 */
public class RaftNode {

    private static final int MAX_ELECTION_TIMEOUT = 300;

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

    private ScheduledExecutorService timerService; //用于定时器的线程池
    private ExecutorService executorService; //普通执行请求的线程池
    private ScheduledFuture electionTimerFuture;

    public RaftNode(List<RaftMessage.Server> servers, RaftMessage.Server localServer) {
        RaftMessage.Configuration.Builder configurationBuilder = RaftMessage.Configuration.newBuilder();
        configurationBuilder.addAllServers(servers);
        this.configuration = configurationBuilder.build();
        this.localServer = localServer;
    }

    public void init() {
        List<RaftMessage.Server> serverList = configuration.getServersList();
        for (RaftMessage.Server server : serverList) {
            if (localServer.equals(server)) {
                continue;
            }
            RaftPeer peer = new RaftPeer(server);
            peerMap.put(server.getServerId(), peer);
        }
        final int NODE_SIZE = peerMap.size();
        final int WAITING_QUEUE_SIZE = 10240;
        executorService = new ThreadPoolExecutor(NODE_SIZE, NODE_SIZE, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(WAITING_QUEUE_SIZE), new ThreadFactoryWithPrefix("Network-IO-Thread-"));
        timerService = Executors.newScheduledThreadPool(2, new ThreadFactoryWithPrefix("Timer-"));
        resetElectionTimer(); // 超时选举定时器
    }

    private void resetElectionTimer(){
        if(electionTimerFuture != null && !electionTimerFuture.isDone()){
            electionTimerFuture.cancel(true);
        }
        electionTimerFuture = timerService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //todo
            }
        }, 0, getElectionTimeoutMs(), TimeUnit.MILLISECONDS);
    }

    /**
     * 开始发起投票
     * 只有状态为candidate才能调用这个函数
     */
    public void startVote() {

    }

    /**
     * 向peer发起投票请求
     * 只有状态为candidate才能调用这个函数
     *
     * @param peer
     */
    public void requestVote(RaftPeer peer) {

    }


    /**
     * 将日志数据先写入到本节点本地，然后调用appendEntities向其他每个节点发起appendEntities的RPC请求
     * 只有状态为Leader才能调用这个函数
     *
     * @param payload 序列化后的数据
     * @return 一半节点都成功复制则return true， 否则一直等待，直至超时，返回false
     */
    public boolean replicate(byte[] payload) {
        return false;
    }

    /**
     * 真正发起appendEntities rpc调用
     */
    public void appendEntities(RaftPeer peer) {

    }

    private int getElectionTimeoutMs(){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int timeout = random.nextInt(MAX_ELECTION_TIMEOUT);
        return timeout;
    }

    /**
     * 负责处理appendEntities请求的响应
     */
    private class AppendEntityResponseCallback implements RpcCallback<RaftMessage.AppendEntityResponse> {
        @Override
        public void success(RaftMessage.AppendEntityResponse appendEntityResponse) {

        }

        @Override
        public void fail(Throwable throwable) {

        }
    }

    /**
     * 负责处理vote请求的响应
     */
    private class VoteResponseCallback implements RpcCallback<RaftMessage.VoteResponse> {
        @Override
        public void success(RaftMessage.VoteResponse voteResponse) {

        }

        @Override
        public void fail(Throwable throwable) {

        }
    }

    private class ThreadFactoryWithPrefix implements ThreadFactory {
        private int id = 0;
        private final String prefix;

        private ThreadFactoryWithPrefix() {
            this.prefix = "Thread-";
        }

        private ThreadFactoryWithPrefix(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName(prefix + (id++));
            return t;
        }
    }


}
