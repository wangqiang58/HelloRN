import NetInfo, { NetInfoState } from '@react-native-community/netinfo';

type NetworkChangeCallback = (isConnected: boolean) => void;

class NetworkManager {
  private static instance: NetworkManager;
  private subscribers: Set<NetworkChangeCallback> = new Set();
  private isConnected: boolean = true;

  private constructor() {
    this.initNetworkListener();
  }

  public static getInstance(): NetworkManager {
    if (!NetworkManager.instance) {
      NetworkManager.instance = new NetworkManager();
    }
    return NetworkManager.instance;
  }

  private initNetworkListener() {
    NetInfo.addEventListener((state: NetInfoState) => {
      this.isConnected = state.isConnected ?? false;
      this.notifySubscribers();
    });
  }

  public addNetworkChangeListener(callback: NetworkChangeCallback) {
    this.subscribers.add(callback);
    // 立即通知当前网络状态
    callback(this.isConnected);
  }

  public removeNetworkChangeListener(callback: NetworkChangeCallback) {
    this.subscribers.delete(callback);
  }

  private notifySubscribers() {
    this.subscribers.forEach(callback => {
      callback(this.isConnected);
    });
  }

  public async getCurrentNetworkState(): Promise<boolean> {
    const state = await NetInfo.fetch();
    return state.isConnected ?? false;
  }
}

export default NetworkManager;