/**
 * ============================================
 * 排序算法集合 - Sorting Algorithms Collection
 * ============================================
 *
 * 每种排序算法都是一个 Generator 函数，
 * 通过 yield 将每一步的比较/交换信息传出，
 * 供可视化模块呈现动画效果。
 *
 * yield 格式: { type: 'compare'|'swap'|'overwrite'|'sorted'|'pivot', indices: [...], array: [...] }
 */

// ======================== 1. 冒泡排序 ========================
function* bubbleSort(arr) {
  const a = [...arr];
  const n = a.length;
  for (let i = 0; i < n - 1; i++) {
    let swapped = false;
    for (let j = 0; j < n - 1 - i; j++) {
      yield { type: 'compare', indices: [j, j + 1], array: a };
      if (a[j] > a[j + 1]) {
        [a[j], a[j + 1]] = [a[j + 1], a[j]];
        swapped = true;
        yield { type: 'swap', indices: [j, j + 1], array: a };
      }
    }
    yield { type: 'sorted', indices: [n - 1 - i], array: a };
    if (!swapped) break;
  }
  // Mark all as sorted
  for (let i = 0; i < n; i++) {
    yield { type: 'sorted', indices: [i], array: a };
  }
}

// ======================== 2. 选择排序 ========================
function* selectionSort(arr) {
  const a = [...arr];
  const n = a.length;
  for (let i = 0; i < n - 1; i++) {
    let minIdx = i;
    for (let j = i + 1; j < n; j++) {
      yield { type: 'compare', indices: [minIdx, j], array: a };
      if (a[j] < a[minIdx]) {
        minIdx = j;
      }
    }
    if (minIdx !== i) {
      [a[i], a[minIdx]] = [a[minIdx], a[i]];
      yield { type: 'swap', indices: [i, minIdx], array: a };
    }
    yield { type: 'sorted', indices: [i], array: a };
  }
  yield { type: 'sorted', indices: [n - 1], array: a };
}

// ======================== 3. 插入排序 ========================
function* insertionSort(arr) {
  const a = [...arr];
  const n = a.length;
  yield { type: 'sorted', indices: [0], array: a };
  for (let i = 1; i < n; i++) {
    let j = i;
    while (j > 0) {
      yield { type: 'compare', indices: [j - 1, j], array: a };
      if (a[j - 1] > a[j]) {
        [a[j - 1], a[j]] = [a[j], a[j - 1]];
        yield { type: 'swap', indices: [j - 1, j], array: a };
        j--;
      } else {
        break;
      }
    }
    yield { type: 'sorted', indices: [j], array: a };
  }
}

// ======================== 4. 希尔排序 ========================
function* shellSort(arr) {
  const a = [...arr];
  const n = a.length;
  let gap = Math.floor(n / 2);
  while (gap > 0) {
    for (let i = gap; i < n; i++) {
      let j = i;
      while (j >= gap) {
        yield { type: 'compare', indices: [j - gap, j], array: a };
        if (a[j - gap] > a[j]) {
          [a[j - gap], a[j]] = [a[j], a[j - gap]];
          yield { type: 'swap', indices: [j - gap, j], array: a };
          j -= gap;
        } else {
          break;
        }
      }
    }
    gap = Math.floor(gap / 2);
  }
  for (let i = 0; i < n; i++) {
    yield { type: 'sorted', indices: [i], array: a };
  }
}

// ======================== 5. 归并排序 ========================
function* mergeSort(arr) {
  const a = [...arr];
  const n = a.length;

  function* mergeSortRec(lo, hi) {
    if (lo >= hi) {
      if (lo === hi) yield { type: 'sorted', indices: [lo], array: a };
      return;
    }
    const mid = Math.floor((lo + hi) / 2);
    yield* mergeSortRec(lo, mid);
    yield* mergeSortRec(mid + 1, hi);
    yield* merge(lo, mid, hi);
  }

  function* merge(lo, mid, hi) {
    const left = a.slice(lo, mid + 1);
    const right = a.slice(mid + 1, hi + 1);
    let i = 0, j = 0, k = lo;

    while (i < left.length && j < right.length) {
      yield { type: 'compare', indices: [lo + i, mid + 1 + j], array: a };
      if (left[i] <= right[j]) {
        a[k] = left[i];
        yield { type: 'overwrite', indices: [k], array: a };
        i++;
      } else {
        a[k] = right[j];
        yield { type: 'overwrite', indices: [k], array: a };
        j++;
      }
      k++;
    }
    while (i < left.length) {
      a[k] = left[i];
      yield { type: 'overwrite', indices: [k], array: a };
      i++; k++;
    }
    while (j < right.length) {
      a[k] = right[j];
      yield { type: 'overwrite', indices: [k], array: a };
      j++; k++;
    }
    for (let idx = lo; idx <= hi; idx++) {
      yield { type: 'sorted', indices: [idx], array: a };
    }
  }

  yield* mergeSortRec(0, n - 1);
}

// ======================== 6. 快速排序 ========================
function* quickSort(arr) {
  const a = [...arr];
  const n = a.length;

  function* quickSortRec(lo, hi) {
    if (lo >= hi) return;
    const pivotIdx = yield* partition(lo, hi);
    yield { type: 'sorted', indices: [pivotIdx], array: a };
    yield* quickSortRec(lo, pivotIdx - 1);
    yield* quickSortRec(pivotIdx + 1, hi);
  }

  function* partition(lo, hi) {
    const pivot = a[hi];
    let i = lo - 1;
    yield { type: 'pivot', indices: [hi], array: a };
    for (let j = lo; j < hi; j++) {
      yield { type: 'compare', indices: [j, hi], array: a };
      if (a[j] < pivot) {
        i++;
        if (i !== j) {
          [a[i], a[j]] = [a[j], a[i]];
          yield { type: 'swap', indices: [i, j], array: a };
        }
      }
    }
    if (i + 1 !== hi) {
      [a[i + 1], a[hi]] = [a[hi], a[i + 1]];
      yield { type: 'swap', indices: [i + 1, hi], array: a };
    }
    return i + 1;
  }

  yield* quickSortRec(0, n - 1);
  for (let i = 0; i < n; i++) {
    yield { type: 'sorted', indices: [i], array: a };
  }
}

// ======================== 7. 堆排序 ========================
function* heapSort(arr) {
  const a = [...arr];
  const n = a.length;

  function* heapify(size, root) {
    let largest = root;
    const left = 2 * root + 1;
    const right = 2 * root + 2;

    if (left < size) {
      yield { type: 'compare', indices: [left, largest], array: a };
      if (a[left] > a[largest]) largest = left;
    }
    if (right < size) {
      yield { type: 'compare', indices: [right, largest], array: a };
      if (a[right] > a[largest]) largest = right;
    }
    if (largest !== root) {
      [a[root], a[largest]] = [a[largest], a[root]];
      yield { type: 'swap', indices: [root, largest], array: a };
      yield* heapify(size, largest);
    }
  }

  // Build max heap
  for (let i = Math.floor(n / 2) - 1; i >= 0; i--) {
    yield* heapify(n, i);
  }

  // Extract elements
  for (let i = n - 1; i >= 0; i--) {
    [a[0], a[i]] = [a[i], a[0]];
    yield { type: 'swap', indices: [0, i], array: a };
    yield { type: 'sorted', indices: [i], array: a };
    yield* heapify(i, 0);
  }
}

// ======================== 8. 计数排序 ========================
function* countingSort(arr) {
  const a = [...arr];
  const n = a.length;
  if (n <= 1) {
    for (let i = 0; i < n; i++) yield { type: 'sorted', indices: [i], array: a };
    return;
  }

  const max = Math.max(...a);
  const min = Math.min(...a);
  const range = max - min + 1;
  const count = new Array(range).fill(0);
  const output = new Array(n);

  // Count occurrences
  for (let i = 0; i < n; i++) {
    count[a[i] - min]++;
  }

  // Cumulative count
  for (let i = 1; i < range; i++) {
    count[i] += count[i - 1];
  }

  // Build output array
  for (let i = n - 1; i >= 0; i--) {
    const idx = --count[a[i] - min];
    output[idx] = a[i];
  }

  // Copy back
  for (let i = 0; i < n; i++) {
    a[i] = output[i];
    yield { type: 'overwrite', indices: [i], array: a };
  }

  for (let i = 0; i < n; i++) {
    yield { type: 'sorted', indices: [i], array: a };
  }
}

// ======================== 9. 基数排序 ========================
function* radixSort(arr) {
  const a = [...arr];
  const n = a.length;
  if (n <= 1) {
    for (let i = 0; i < n; i++) yield { type: 'sorted', indices: [i], array: a };
    return;
  }

  const max = Math.max(...a.map(x => Math.abs(x)));
  const hasNeg = Math.min(...a) < 0;

  // Handle negative numbers: shift all to non-negative
  if (hasNeg) {
    const min = Math.min(...a);
    for (let i = 0; i < n; i++) a[i] -= min;
  }

  let exp = 1;
  while (Math.floor(max / exp) > 0) {
    const count = new Array(10).fill(0);
    const output = new Array(n);

    for (let i = 0; i < n; i++) {
      const digit = Math.floor(Math.abs(a[i]) / exp) % 10;
      count[digit]++;
    }

    for (let i = 1; i < 10; i++) {
      count[i] += count[i - 1];
    }

    for (let i = n - 1; i >= 0; i--) {
      const digit = Math.floor(Math.abs(a[i]) / exp) % 10;
      output[--count[digit]] = a[i];
    }

    for (let i = 0; i < n; i++) {
      a[i] = output[i];
      yield { type: 'overwrite', indices: [i], array: a };
    }

    exp *= 10;
  }

  // Shift back if had negatives
  if (hasNeg) {
    const min = Math.min(...arr);
    for (let i = 0; i < n; i++) a[i] += min;
    for (let i = 0; i < n; i++) yield { type: 'overwrite', indices: [i], array: a };
  }

  for (let i = 0; i < n; i++) {
    yield { type: 'sorted', indices: [i], array: a };
  }
}

// ======================== 10. Bogo 排序（猴子排序） ========================
function* bogoSort(arr) {
  const a = [...arr];
  const n = a.length;

  function isSorted(array) {
    for (let i = 1; i < array.length; i++) {
      if (array[i - 1] > array[i]) return false;
    }
    return true;
  }

  function shuffle(array) {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
  }

  let iterations = 0;
  const maxIterations = 5000; // Safety limit

  while (!isSorted(a) && iterations < maxIterations) {
    shuffle(a);
    iterations++;
    // Mark a few random indices as comparing
    const randIdx = Math.floor(Math.random() * n);
    yield { type: 'swap', indices: [randIdx, (randIdx + 1) % n], array: a };
  }

  if (iterations >= maxIterations) {
    // Fallback: just use insertion sort for sanity
    const sorted = [...arr].sort((x, y) => x - y);
    for (let i = 0; i < n; i++) {
      a[i] = sorted[i];
      yield { type: 'overwrite', indices: [i], array: a };
    }
  }

  for (let i = 0; i < n; i++) {
    yield { type: 'sorted', indices: [i], array: a };
  }
}

// ======================== 算法元数据 ========================
const ALGORITHMS = {
  bubble: {
    name: '冒泡排序 (Bubble Sort)',
    generator: bubbleSort,
    time: 'O(n²)',
    space: 'O(1)',
    stable: true,
    description: '冒泡排序通过重复遍历要排序的列表，比较相邻元素并交换顺序错误的元素。每一轮遍历都会将最大的未排序元素"冒泡"到正确位置。',
    code: `function bubbleSort(arr) {
  const n = arr.length;
  for (let i = 0; i < n - 1; i++) {
    let swapped = false;
    for (let j = 0; j < n - 1 - i; j++) {
      if (arr[j] > arr[j + 1]) {
        [arr[j], arr[j + 1]] = [arr[j + 1], arr[j]];
        swapped = true;
      }
    }
    if (!swapped) break;  // 优化：提前结束
  }
  return arr;
}`
  },
  selection: {
    name: '选择排序 (Selection Sort)',
    generator: selectionSort,
    time: 'O(n²)',
    space: 'O(1)',
    stable: false,
    description: '选择排序每次从未排序部分找到最小（或最大）元素，将其放到已排序序列的末尾。算法简单直观，但无论数据如何，比较次数始终为 O(n²)。',
    code: `function selectionSort(arr) {
  const n = arr.length;
  for (let i = 0; i < n - 1; i++) {
    let minIdx = i;
    for (let j = i + 1; j < n; j++) {
      if (arr[j] < arr[minIdx]) minIdx = j;
    }
    if (minIdx !== i) {
      [arr[i], arr[minIdx]] = [arr[minIdx], arr[i]];
    }
  }
  return arr;
}`
  },
  insertion: {
    name: '插入排序 (Insertion Sort)',
    generator: insertionSort,
    time: 'O(n²)',
    space: 'O(1)',
    stable: true,
    description: '插入排序的工作原理是通过构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。对于接近有序的数据，效率很高。',
    code: `function insertionSort(arr) {
  const n = arr.length;
  for (let i = 1; i < n; i++) {
    let j = i;
    while (j > 0 && arr[j - 1] > arr[j]) {
      [arr[j - 1], arr[j]] = [arr[j], arr[j - 1]];
      j--;
    }
  }
  return arr;
}`
  },
  shell: {
    name: '希尔排序 (Shell Sort)',
    generator: shellSort,
    time: 'O(n log n) ~ O(n²)',
    space: 'O(1)',
    stable: false,
    description: '希尔排序是插入排序的改进版，通过将原始数据分割成多个子序列分别进行插入排序，然后逐步缩小子序列的间隔，最终使整个序列基本有序。',
    code: `function shellSort(arr) {
  const n = arr.length;
  for (let gap = Math.floor(n / 2); gap > 0; gap = Math.floor(gap / 2)) {
    for (let i = gap; i < n; i++) {
      let j = i;
      while (j >= gap && arr[j - gap] > arr[j]) {
        [arr[j - gap], arr[j]] = [arr[j], arr[j - gap]];
        j -= gap;
      }
    }
  }
  return arr;
}`
  },
  merge: {
    name: '归并排序 (Merge Sort)',
    generator: mergeSort,
    time: 'O(n log n)',
    space: 'O(n)',
    stable: true,
    description: '归并排序采用分治策略，将数组递归地分成两半分别排序，然后将两个有序子数组合并为一个有序数组。性能稳定，始终为 O(n log n)。',
    code: `function mergeSort(arr) {
  if (arr.length <= 1) return arr;
  const mid = Math.floor(arr.length / 2);
  const left = mergeSort(arr.slice(0, mid));
  const right = mergeSort(arr.slice(mid));

  const result = [];
  let i = 0, j = 0;
  while (i < left.length && j < right.length) {
    result.push(left[i] <= right[j] ? left[i++] : right[j++]);
  }
  return [...result, ...left.slice(i), ...right.slice(j)];
}`
  },
  quick: {
    name: '快速排序 (Quick Sort)',
    generator: quickSort,
    time: 'O(n log n) 平均 / O(n²) 最坏',
    space: 'O(log n)',
    stable: false,
    description: '快速排序也采用分治策略，选择一个基准元素（pivot），将小于基准的元素移到左边，大于的移到右边，然后递归地对左右子序列排序。实际应用中通常最快。',
    code: `function quickSort(arr, lo = 0, hi = arr.length - 1) {
  if (lo >= hi) return;
  const pivot = arr[hi];
  let i = lo - 1;
  for (let j = lo; j < hi; j++) {
    if (arr[j] < pivot) {
      i++;
      [arr[i], arr[j]] = [arr[j], arr[i]];
    }
  }
  [arr[i + 1], arr[hi]] = [arr[hi], arr[i + 1]];
  quickSort(arr, lo, i);
  quickSort(arr, i + 2, hi);
  return arr;
}`
  },
  heap: {
    name: '堆排序 (Heap Sort)',
    generator: heapSort,
    time: 'O(n log n)',
    space: 'O(1)',
    stable: false,
    description: '堆排序利用堆这种数据结构，将数组构建成最大堆，然后反复将堆顶（最大值）与末尾交换，并调整堆结构。是一种原地排序算法。',
    code: `function heapSort(arr) {
  const n = arr.length;

  function heapify(size, root) {
    let largest = root;
    const left = 2 * root + 1, right = 2 * root + 2;
    if (left < size && arr[left] > arr[largest]) largest = left;
    if (right < size && arr[right] > arr[largest]) largest = right;
    if (largest !== root) {
      [arr[root], arr[largest]] = [arr[largest], arr[root]];
      heapify(size, largest);
    }
  }

  for (let i = Math.floor(n / 2) - 1; i >= 0; i--) heapify(n, i);
  for (let i = n - 1; i >= 0; i--) {
    [arr[0], arr[i]] = [arr[i], arr[0]];
    heapify(i, 0);
  }
  return arr;
}`
  },
  counting: {
    name: '计数排序 (Counting Sort)',
    generator: countingSort,
    time: 'O(n + k)',
    space: 'O(k)',
    stable: true,
    description: '计数排序是一种非比较排序，通过统计每个元素出现的次数，然后根据计数信息将元素放到正确位置。k 是数据范围，适合数据范围不大的整数排序。',
    code: `function countingSort(arr) {
  const max = Math.max(...arr);
  const min = Math.min(...arr);
  const range = max - min + 1;
  const count = new Array(range).fill(0);
  const output = new Array(arr.length);

  for (const x of arr) count[x - min]++;
  for (let i = 1; i < range; i++) count[i] += count[i - 1];
  for (let i = arr.length - 1; i >= 0; i--) {
    output[--count[arr[i] - min]] = arr[i];
  }
  for (let i = 0; i < arr.length; i++) arr[i] = output[i];
  return arr;
}`
  },
  radix: {
    name: '基数排序 (Radix Sort)',
    generator: radixSort,
    time: 'O(d × (n + k))',
    space: 'O(n + k)',
    stable: true,
    description: '基数排序是按位排序的非比较算法，从最低有效位到最高有效位依次对数据进行排序。d 是位数，k 是基数（通常为10）。适合整数或固定长度字符串。',
    code: `function radixSort(arr) {
  const max = Math.max(...arr.map(x => Math.abs(x)));
  for (let exp = 1; Math.floor(max / exp) > 0; exp *= 10) {
    const count = new Array(10).fill(0);
    const output = new Array(arr.length);

    for (const x of arr) count[Math.floor(x / exp) % 10]++;
    for (let i = 1; i < 10; i++) count[i] += count[i - 1];
    for (let i = arr.length - 1; i >= 0; i--) {
      output[--count[Math.floor(arr[i] / exp) % 10]] = arr[i];
    }
    for (let i = 0; i < arr.length; i++) arr[i] = output[i];
  }
  return arr;
}`
  },
  bogo: {
    name: '🐢 Bogo 排序 (Bogo Sort)',
    generator: bogoSort,
    time: 'O((n+1)!)',
    space: 'O(1)',
    stable: false,
    description: 'Bogo 排序（猴子排序）是一种基于"随机打乱"的排序算法：每次随机打乱数组，检查是否有序，如果不是则继续打乱。最坏情况下永远无法完成——娱乐性质为主！',
    code: `function bogoSort(arr) {
  while (!isSorted(arr)) {
    shuffle(arr);  // 随机打乱
  }
  return arr;
}
// 时间复杂度: O((n+1)!) — 极度低效！
// 请勿在生产环境使用 😄`
  }
};
