<template>
    <div class="container has-margin-top">
        <div class="card">
            <div class="card-body">
                <slot>

                </slot>
            </div>
        </div>
    </div>
</template>
<script>

export default {
    name: "MyContainer",
}
</script>
<style scoped>
.has-margin-top {
    margin-top: 20px;
}
</style>