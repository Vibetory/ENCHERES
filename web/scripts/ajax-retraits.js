async function getRetrait(articleARetirer) {
    return await getData(`retrait/${articleARetirer}`);
}