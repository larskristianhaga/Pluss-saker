<script setup lang="ts">
import axios from "axios";

const props = defineProps<{ newsPaperName: string, url: string, targetImage: string, targetCSS: string }>()
let numberOfPlussArticles: number

axios.get(props.url)
    .then((response: any) => {
      const re = new RegExp(props.targetCSS, 'g');
      numberOfPlussArticles = (response.data.match(re) || []).length
    })
    .catch((error: string) => {
      console.log("Something went wrong... " + error)
    })

</script>

<template>
  <div class="card">
    <div class="card-header">
      <p>{{ this.newsPaperName }}</p>
    </div>

    <div class="card-body">
      <p>{{ this.url }}</p>
      <p>{{ this.targetImage }}</p>
      <p>{{ this.targetCSS }}</p>
      <p>Number of pluss articles {{ numberOfPlussArticles }}</p>
    </div>
  </div>

</template>

<style>
.card {
  width: 250px;
  border: 1px solid;
}

.card-header {
  display: flex;
  justify-content: center;
  font-weight: bold;
}
</style>
