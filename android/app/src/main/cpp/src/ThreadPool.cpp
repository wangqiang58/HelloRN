//
// Created by wangqiang on 2024/12/28.
//
#include "ThreadPool.h"


ThreadPool::ThreadPool() : stop(false) {
    for (size_t i = 0; i < 5; i++) {
        threads.emplace_back([this]() {
            while (true) {
                std::function<void()> task;
                {
                    std::unique_lock<std::mutex> lock(queueMutex);
                    condition.wait(lock, [this]() { return stop || !tasks.empty(); });
                    if (stop && tasks.empty()) {
                        return;
                    }
                    task = std::move(tasks.front());
                    tasks.pop();
                }
                task();
            }
        });
    }
}

ThreadPool::~ThreadPool() {
    {
        std::unique_lock<std::mutex> lock(queueMutex);
        stop = true;
    }
    condition.notify_all();
    for (std::thread &thread: threads) {
        if (thread.joinable()) {
            thread.join();
        }
    }
}

