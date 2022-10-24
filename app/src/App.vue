<script setup lang="ts">
import NewsPaper from './components/NewsPaper.vue'
</script>

<template>
  <div class="container">
    <div class="header">

    </div>
    <div class="news-papers">
      <suspense>
        <NewsPaper news-paper-name="VG" url="https://www.vg.no/"
                   target-image="https://www.vg.no/vgc/frimand/gfx/icons/pluss.svg?x=2"
                   targetCSS="type-icon pluss-icon"/>
      </suspense>
      <suspense>
        <NewsPaper news-paper-name="Amta" url="https://www.amta.no/"
                   target-image=""
                   targetCSS='data-adppaywall="true"'/>
      </suspense>
      <suspense>
        <NewsPaper news-paper-name="Avisa Oslo" url="https://www.ao.no/"
                   target-image=""
                   targetCSS='data-adppaywall="true"'/>
      </suspense>
    </div>
  </div>
</template>

<style>
.news-papers {
  display: flex;
  justify-content: space-evenly;
}
</style>
