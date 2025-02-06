const base_url = "https://api.thecatapi.com"



export function getCatList() {
    return fetch(base_url + "/v1/images/search?limit=5").then((response) => response.json())
}

export function getNewsList(page:number) {
    return fetch('https://api.qqsuu.cn/api/dm-woman?num=10&page='+page).then((response) => response.json())
}

export function getMovices(){
    return fetch('http://v.juhe.cn/toutiao/index').then((response) => response.json())
}
