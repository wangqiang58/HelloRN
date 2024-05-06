import ACTION_TYPES from "./actionTypes";

export const addToGroceryList = (item) => {
    return { type: 'ADD', item };
}

export const removeFromGroceryList = (id) => {
    return { type: 'REMOVE', id };
}

