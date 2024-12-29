//
// Created by wangqiang on 2024/12/28.
//

#ifndef HELLORN_THREADPOOL_H
#define HELLORN_THREADPOOL_H

#include "stdlib.h"
#include "queue"
#include "mutex"
#include "thread"
#include <future>


class ThreadPool {
public:
    ThreadPool();

    ~ThreadPool();

    template<typename F>
    void enqueue(F &&f) {
        std::function<void()> task = std::forward<F>(f);
        {
            std::unique_lock<std::mutex> lock(queueMutex);
            if (stop) {
                throw std::runtime_error("enqueue on stopped ThreadPool");
            }
            tasks.emplace(std::move(task));
        }
        condition.notify_one();
    }

private:
    std::vector<std::thread> threads;
    std::queue<std::function<void()>> tasks;
    std::mutex queueMutex;
    std::condition_variable condition;
    std::atomic<bool> stop;
};


#endif //HELLORN_THREADPOOL_H
